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
            const catsWithName=this.createCategoriesWithName(this.selectedCategories);
            this.props.setCategories(this.selectedCategories,catsWithName);
        }else if(props.removeSubcategory){
            this.selectedSubcategories=this.selectedSubcategories.filter((x)=>x!=props.removeSubcategory);
            this.setState({['selectedSubcategories']:this.selectedSubcategories});
            let subsWithName=this.createSubcategoriesWithName(this.selectedSubcategories);
            this.props.setSubcategories(this.selectedSubcategories,subsWithName);
        }
    }

    loadCategories=()=>{
        getAllCategories((success,data)=>{
            if(success){
                this.setState({['categories']: data.slice(1)});
                console.log(data);
            }else{
                console.log("Cannot fetch categories")
            }
        });
    }

    selectCategory=(cat)=>{
        if(this.selectedCategories.includes(cat.id)){
            this.selectedCategories=this.selectedCategories.filter((x)=>x!=cat.id);
        }else{
            this.selectedCategories.push(cat.id);
        }
        this.setState({['selectedCategories']:this.selectedCategories});
        const catsWithName=this.createCategoriesWithName(this.selectedCategories);
        this.props.setCategories(this.selectedCategories,catsWithName);
    }

    createCategoriesWithName=(sCats)=>{
        let catsWithName=[]
        for(const c in sCats){
            const crrCat=this.state.categories.filter((x)=>x.id===sCats[c])[0];
            catsWithName.push({id:crrCat.id,name:crrCat.name})
        }
        return catsWithName;
    }

    selectSubcategory=(sub)=>{
        if(this.selectedSubcategories.includes(sub.id)){
            this.selectedSubcategories=this.selectedSubcategories.filter((x)=>x!=sub.id);
        }else{
            this.selectedSubcategories.push(sub.id);
        }
        this.setState({['selectedSubcategories']:this.selectedSubcategories});
        let subsWithName=this.createSubcategoriesWithName(this.selectedSubcategories);
        this.props.setSubcategories(this.selectedSubcategories,subsWithName);
    }

    createSubcategoriesWithName=(sSubs)=>{
        let subsWithName=[]
        for(const s in sSubs){
            const crrSub=null;
            for(const c in this.state.categories){
                if(this.state.categories[c].subcategories.filter((x)=>x.id===sSubs[s]).length!=0)
                    crrSub=this.state.categories[c].subcategories.filter((x)=>x.id===sSubs[s])[0]
            }
            if(crrSub)
                subsWithName.push({id:crrSub.id,name:crrSub.name,cat:crrSub.category.name})
        }        
        return subsWithName;
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
                           onClick={()=>this.selectCategory(category)}>{category.name}</div>
                        <img className="categoriesButtonIcon"
                            onClick={()=>this.expandCategory(category.id)}
                            src={this.state.expandedCategories.includes(category.id)?
                            "/images/category_selected.svg":"/images/category_unselected.svg"}
                        />
                    </div>
                    
                    {this.state.expandedCategories.includes(category.id)&&
                        category.subcategories.map(sub=>
                                <div className={this.state.selectedSubcategories.includes(sub.id)?"subcategoryButtonSelected":"subcategoryButton"}
                                   onClick={()=>this.selectSubcategory(sub)}>{sub.name}</div>
                            )}
                        </div>
                    )}
            </div>
        )
    }
}

export default CategorySubcategoryMenu