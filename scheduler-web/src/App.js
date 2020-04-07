import React, { Component } from 'react';
import { BrowserRouter as Router, Route } from "react-router-dom";
import TodoApp from './components/todo/TodoApp'
import TodoComponent from './components/todo/TodoComponent'
import './App.css';
import './bootstrap.css';

class App extends Component {
  render() {
    return (
      <Router>
        <div className="App">
            <Route path="/" exact component={TodoApp} />
            <Route path="/todos/:id" component={TodoComponent} />
        </div>
      </Router>
    );
  }
}

export default App;