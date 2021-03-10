import React from 'react';
import { getLastChance, getNewArrivals } from '../apiConsumer/itemFetchConsumer';
import {getLinks} from '../apiConsumer/imageLinker';
import ItemElement from './itemELement';
import { Link } from 'react-router-dom';


class Home extends React.Component{
    constructor(props){
        super(props);

        this.state={
            items:[],
            activeTab:""
        }
    }

    componentDidMount(){
        this.lastChanceClick();
    }

    lastChanceClick=()=>{
        getLastChance(0,8,(bool,data)=>{
            if(bool){
                this.setState({items:data,activeTab:'lastChance'});
            }else{
                console.log("Cannot fetch data");
            }
        });
    }

    newArrivalsClick=()=>{
        getNewArrivals(0,8,(bool,data)=>{
            if(bool){
                this.setState({items:data,activeTab:'newArrivals'});
            }else{
                console.log("Cannot fetch data");
            }
        });     
    }

    render(){
        return(
            <div className="homeContainer">
                <div  className="homePageNavigator">
                    <span className={this.state.activeTab==="lastChance"?"highlightLinkStyle":"linkStyle"} onClick={this.lastChanceClick}>Last Chance</span>
                    <span className={this.state.activeTab==="newArrivals"?"highlightLinkStyle":"linkStyle"} onClick={this.newArrivalsClick}>New Arrivals</span>
                </div>
                <hr className="solid"></hr>
                <div className="gridItemContainer">
                    {this.state.items.map(item=><Link to={"/item?id="+item.id}><ItemElement item={item} type="grid"/></Link>)}
                </div>
            </div>
        )
    }

}

export default Home