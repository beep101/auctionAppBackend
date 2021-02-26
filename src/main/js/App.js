import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import {AuthProvider} from '../../../ui/src/context';
import Wrap from '../../../ui/src/components/wrap';

export class App extends Component {
    render(){
        return (
            <AuthProvider>
                <Wrap/>
            </AuthProvider>
        );
    }
}

export default App;

ReactDOM.render(<App />,document.getElementById("app"));