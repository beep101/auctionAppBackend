import React from 'react'
import { getAllCategories } from '../apiConsumer/categoryConsumer';

class CategorySubcategoryMenu extends React.Component{
    constructor(props){
        super(props);
        
        if(props.preset==null)
            this.selectedCategories=[];
        else
            this.selectedCategories=[props.preset]
        this.selectedSubcategories=[];

        this.state={
            categories:[],
            selectedCategories:this.selectedCategories,
            selectedSubcategories:[],
            expandedCategories:[]
        }
    }

    componentDidMount=()=>{
        this.loadCategories();
    }

    componentWillReceiveProps=(props)=>{
        if(props.removeCategory){
            this.selectedCategories=this.selectedCategories.filter((x)=>x!=props.removeCategory);
            this.setState({['selectedCategories']:this.selectedCategories});
            this.props.setCategories(this.selectedCategories);
        }else if(props.removeSubcategory){
            this.selectedSubcategories=this.selectedSubcategories.filter((x)=>x!=props.removeSubcategory);
            this.setState({['selectedSubcategories']:this.selectedSubcategories});
            this.props.setSubcategories(this.selectedSubcategories);
        }
    }

    loadCategories=()=>{
        getAllCategories((success,data)=>{
            if(success){
                this.setState({['categories']: data.slice(1)});
            }else{
                console.log("Cannot fetch categories")
            }
        });
    }

    selectCategory=(cat)=>{
        if(this.selectedCategories.includes(cat)){
            this.selectedCategories=this.selectedCategories.filter((x)=>x!=cat);
        }else{
            this.selectedCategories.push(cat);
        }
        this.setState({['selectedCategories']:this.selectedCategories});
        this.props.setCategories(this.selectedCategories);
    }

    selectSubcategory=(sub)=>{
        if(this.selectedSubcategories.includes(sub)){
            this.selectedSubcategories=this.selectedSubcategories.filter((x)=>x!=sub);
        }else{
            this.selectedSubcategories.push(sub);
        }
        this.setState({['selectedSubcategories']:this.selectedSubcategories});
        this.props.setSubcategories(this.selectedSubcategories);
    }

    expandCategory=(cat)=>{
        if(this.state.expandedCategories.includes(cat))
            this.setState({['expandedCategories']:this.state.expandedCategories.filter((x)=>x!=cat)});
        else
            this.setState({['expandedCategories']:[...this.state.expandedCategories,cat]});
    }

    render(){
        return(
            <div className="categoriesList">
                <div className="categoryButtonTitle">PRODUCT CATEGORIES</div>
                {this.state.categories.map(category=>
                <div>
                    <div className="categoryButtonContainer">
                        <div className={this.state.selectedCategories.includes(category.id)?"categoryButton categoryButtonSelected":"categoryButton"}
                           onClick={()=>this.selectCategory(category.id)}>{category.name}</div>
                        <img className="categoriesButtonIcon"
                            onClick={()=>this.expandCategory(category.id)}
                            src={this.state.expandedCategories.includes(category.id)?
                            "/images/category_selected.svg":"/images/category_unselected.svg"}
                        />
                    </div>
                    
                    {this.state.expandedCategories.includes(category.id)&&
                        category.subcategories.map(sub=>
                                <div className={this.state.selectedSubcategories.includes(sub.id)?"subcategoryButtonSelected":"subcategoryButton"}
                                   onClick={()=>this.selectSubcategory(sub.id)}>{sub.name}</div>
                            )}
                        </div>
                    )}
            </div>
        )
    }
}

export default CategorySubcategoryMenu