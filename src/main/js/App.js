import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import Header from '../../../ui/src/components/Header';
export class App extends Component {
    render(){
        return (
            <div>
                <Header />
                <h1> Welcome to React</h1>
            </div>
        );
    }
}

export default App;

ReactDOM.render(<App />,document.getElementById("app"));