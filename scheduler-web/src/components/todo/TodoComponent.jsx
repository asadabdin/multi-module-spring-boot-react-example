import React, {Component} from 'react'
import moment from 'moment'
import {ErrorMessage, Field, Form, Formik} from 'formik';
import TodoDataService from '../../api/todo/TodoDataService.js'

class TodoComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
            id: this.props.match.params.id,
            title: '',
            description: '',
            dueDate: '',
            resolvedAt: '',
            priority: '',
            status: ''
        };

        this.onSubmit = this.onSubmit.bind(this);
        this.validate = this.validate.bind(this);
    }

    componentDidMount() {
        console.log(this.state);
        TodoDataService.getTodo(this.state.id)
            .then(
                response => {
                    //console.log(response);
                    this.setState({
                            id: response.data.id,
                            title: response.data.title,
                            description: response.data.description,
                            dueDate: moment(response.data.dueDate).format('YYYY-MM-DD'),
                            resolvedAt: moment(response.data.resolvedAt).format('YYYY-MM-DD'),
                            priority: response.data.priority,
                            status: response.data.status
                        }
                    )
                }
            )
    }

    validate(values) {
        let errors = {};
        if (!values.description) {
            errors.description = 'Enter a Description'
        } else if (values.description.length < 5) {
            errors.description = 'Enter atleast 5 Characters in Description'
        }

        if (!moment(values.dueDate).isValid()) {
            errors.dueDate = 'Enter a valid Target Date'
        }

        if (values.priority > 10 && values.priority < 0) {
            errors.priority = 'Enter a valid priority between 1 to 10'
        }

        return errors
    }

    onSubmit(values) {
        console.log("on Submit", values);
        let patch = [];

        function addPatchOperation(value, patch, path) {
            patch.push({
                "op": "replace",
                "path": path,
                "value": value
            })
        }

        if (null != this.state.title) {
            addPatchOperation(this.state.title, patch, "/title");
        }
        if (null != this.state.description) {
            addPatchOperation(this.state.description, patch, "/description");
        }
        if (null != this.state.dueDate) {
            addPatchOperation(moment(this.state.dueDate).format('YYYY-MM-DDTHH:mm:ss'), patch, "/dueDate");
        }
        if (null != this.state.resolvedAt) {
            addPatchOperation(moment(this.state.resolvedAt).format('YYYY-MM-DDTHH:mm:ss'), patch, "/resolvedAt");
        }
        if (null != this.state.priority) {
            addPatchOperation(this.state.priority, patch, "/priority");
        }
        if (null != this.state.status) {
            addPatchOperation(this.state.status, patch, "/status");
        }

        if (patch.length > 0) {
            console.log("patch", patch);
            TodoDataService.patchTodo(this.state.id, patch)
                .then(() => this.props.history.push('/'));
        }
    }

    render() {
        let {title, description, dueDate, resolvedAt, priority, status} = this.state;
        return (
            <div>
                <h1>Todo</h1>
                <div className="container">
                    <Formik
                        initialValues={{title, description, dueDate, resolvedAt, priority, status}}
                        onSubmit={this.onSubmit}
                        validateOnChange={false}
                        validateOnBlur={false}
                        validate={this.validate}
                        enableReinitialize={true}>
                        {
                            (props) => (
                                <Form>
                                    <ErrorMessage name="description" component="div" className="alert alert-warning"/>
                                    <ErrorMessage name="dueDate" component="div" className="alert alert-warning"/>
                                    <ErrorMessage name="priority" component="div" className="alert alert-warning"/>
                                    <fieldset className="form-group">
                                        <label>Title</label>
                                        <Field className="form-control" type="text" name="title"
                                               value={this.state.title} onChange={(event) => this.setState({title: event.target.value})}/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Description</label>
                                        <Field className="form-control" type="text" name="description"
                                               value={this.state.description} onChange={(event) => this.setState({description: event.target.value})}/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Due Date</label>
                                        <Field className="form-control" type="date" name="dueDate"
                                               value={this.state.dueDate} onChange={(event) => this.setState({dueDate: event.target.value})}/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Resolved Date</label>
                                        <Field className="form-control" type="date" name="resolvedAt"
                                               value={this.state.resolvedAt} onChange={(event) => this.setState({resolvedAt: event.target.value})}/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Priority</label>
                                        <Field className="form-control" type="number" name="priority"
                                               value={this.state.priority} onChange={(event) => this.setState({priority: event.target.value})}/>
                                    </fieldset>
                                    <fieldset className="form-group">
                                        <label>Status</label>
                                        <select defaultValue={this.state.status}>
                                            <option value='open'>Open</option>
                                            <option value='in_process'>In Process</option>
                                            <option value='closed'>Closed</option>
                                            <option value='cancelled'>Cancelled</option>
                                        </select>
                                    </fieldset>
                                    <button className="btn btn-success" type="submit">Save</button>
                                </Form>
                            )
                        }
                    </Formik>
                </div>
            </div>
        )
    }
}

export default TodoComponent