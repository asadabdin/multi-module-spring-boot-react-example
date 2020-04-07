import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import TodoDataService from '../../api/todo/TodoDataService.js'
import Pagination from "react-js-pagination";
import moment from 'moment'

class ListTodosComponent extends Component {
    constructor(props) {
        console.log('constructor');
        super(props);
        this.state = {
            todos: [],
            message: null,
            size: 10,
            count: 0,
            activePage: 1,
            pageRequest: {
                "criteria": {
                    "dueDateFrom": "2020-01-06T16:04:12.860Z",
                    "dueDateTo": null,
                    "status": "open"
                },
                "page": 0,
                "pageSize": 5,
                "sort": {
                    "orders": [
                        {
                            "direction": "DESC",
                            "property": "priority"
                        },
                        {
                            "direction": "DESC",
                            "property": "dueDate"
                        }
                    ]
                }
            }
        };
        this.deleteTodoClicked = this.deleteTodoClicked.bind(this);
        this.updateTodoClicked = this.updateTodoClicked.bind(this);
        this.refreshTodos = this.refreshTodos.bind(this);
        this.searchToDo = this.searchToDo.bind(this);
    }

    componentWillUnmount() {
        console.log('componentWillUnmount')
    }

    shouldComponentUpdate(nextProps, nextState) {
        console.log('shouldComponentUpdate');
        console.log(nextProps);
        console.log(nextState);
        return true
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.state.pageRequest.page !== prevState.pageRequest.page) {
            this.searchToDo(this.state.pageRequest);
        }
    }

    componentDidMount() {
        console.log('componentDidMount');
        this.searchToDo(this.state.pageRequest);
        console.log(this.state)
    }

    refreshTodos() {
        TodoDataService.retrieveLatest()
            .then(
                response => {
                    //console.log(response);
                    this.setState({ todos: response.data.content, size: response.data.size - 45, count: response.data.totalElements, activePage: response.data.number + 1 })
                }
            )
    }

    searchToDo(pageRequest) {
        console.log("Searching with ", pageRequest);
        TodoDataService.searchTodo(pageRequest)
            .then(
                response => {
                    //console.log(response);
                    this.setState({ todos: response.data.content, size: response.data.size, count: response.data.totalElements, activePage: response.data.number + 1 })
                }
            )
    }

    deleteTodoClicked(id) {
        TodoDataService.deleteTodo(id)
            .then(
                response => {
                    this.setState({ message: `Delete of todo ${id} Successful` });
                    this.refreshTodos()
                }
            )

    }

    updateTodoClicked(id) {
        console.log('update ' + id);
        this.props.history.push(`/todos/${id}`)
    }

    handlePageChange(pageNumber) {
        console.log(`active page is ${pageNumber}`);
        this.setState({activePage: pageNumber, pageRequest: {...this.state.pageRequest, page: pageNumber -1}});
    }

    render() {
        console.log('render');
        return (

            <div>
                <h1>List Todos</h1>
                {this.state.message && <div class="alert alert-success">{this.state.message}</div>}
                <div className="container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Title</th>
                                <th>Description</th>
                                <th>Target Date</th>
                                <th>Resolved Date</th>
                                <th>Priority</th>
                                <th>Status</th>
                                <th>Update</th>
                                <th>Delete</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.todos.map(
                                    todo =>
                                        <tr key={todo.id}>
                                            <td>{todo.title}</td>
                                            <td>{todo.description}</td>
                                            <td>{moment(todo.dueDate).format('YYYY-MM-DD')}</td>
                                            <td>{moment(todo.resolvedAt).format('YYYY-MM-DD')}</td>
                                            <td>{todo.priority}</td>
                                            <td>{todo.status}</td>
                                            <td><button className="btn btn-success" onClick={() => this.updateTodoClicked(todo.id)}>Update</button></td>
                                            <td><button className="btn btn-warning" onClick={() => this.deleteTodoClicked(todo.id)}>Delete</button></td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>
                    <div>
                        <Pagination itemClass="page-item"
                                    linkClass="page-link"

                            activePage={this.state.activePage}
                            itemsCountPerPage={this.state.size}
                            totalItemsCount={this.state.count}
                            onChange={this.handlePageChange.bind(this)}
                        />
                    </div>
                </div>
            </div>
        )
    }
}

export default withRouter(ListTodosComponent)