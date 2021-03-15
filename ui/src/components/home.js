import React from 'react';
import { getLastChance, getNewArrivals } from '../apiConsumer/itemFetchConsumer';
import ItemElement from './itemELement';


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
        getLastChance(0,6,(bool,data)=>{
            if(bool){
                this.setState({items:data,activeTab:'lastChance'});
            }else{
                console.log("Cannot fetch data");
            }
        });
    }

    newArrivalsClick=()=>{
        getNewArrivals(0,6,(bool,data)=>{
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
                    <span className={this.state.activeTab==="lastChance"?"highlightLinkStyle highlightLinkStyleBorder":"linkStyle"} onClick={this.lastChanceClick}>Last Chance</span>
                    <span className={this.state.activeTab==="newArrivals"?"highlightLinkStyle highlightLinkStyleBorder":"linkStyle"} onClick={this.newArrivalsClick}>New Arrivals</span>
                </div>
                <div className="gridItemContainer">
                    {this.state.items.map(item=><ItemElement link={"/item?id="+item.id} item={item} type="grid"/>)}
                </div>
            </div>
        )
    }

}

export default Home