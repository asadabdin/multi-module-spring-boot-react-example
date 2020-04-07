import React, {Component} from 'react'
import ListTodosComponent from './ListTodosComponent.jsx'

class TodoApp extends Component {
    render() {
        return (
            <div className="TodoApp">
                <ListTodosComponent/>
            </div>
        )
    }
}

export default TodoApp