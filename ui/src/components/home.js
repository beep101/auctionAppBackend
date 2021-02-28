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
            click:""
        }
    }

    componentDidMount(){
        this.lastChanceClick();
    }

    lastChanceClick=()=>{
        getLastChance(0,8,(bool,data)=>{
            if(bool){
                this.setState({items:data,click:'lastChance'});
            }else{
                console.log("Cannot fetch data");
            }
        });
    }

    newArrivalsClick=()=>{
        getNewArrivals(0,8,(bool,data)=>{
            if(bool){
                this.setState({items:data,click:'newArrivals'});
            }else{
                console.log("Cannot fetch data");
            }
        });     
    }

    render(){
        return(
            <div class="homeContainer">
                <div  class="homePageNavigator">
                    <span class={this.state.click=="lastChance"?"highlightLinkStyle":"linkStyle"} onClick={this.lastChanceClick}>Last Chance</span>
                    <span class={this.state.click=="newArrivals"?"highlightLinkStyle":"linkStyle"} onClick={this.newArrivalsClick}>New Arrivals</span>
                </div>
                <hr class="solid"></hr>
                <div class="gridItemContainer">
                    {this.state.items.map(item=><Link to={"/item?id="+item.id}><ItemElement item={item} type="grid"/></Link>)}
                </div>
            </div>
        )
    }

}

export default Home