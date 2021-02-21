import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import Wrap from '../../../ui/src/components/wrap';

export class App extends Component {
    render(){
        return (
            <div>
                <Wrap/>
            </div>
        );
    }
}

export default App;

ReactDOM.render(<App />,document.getElementById("app"));