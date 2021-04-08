import React, { useEffect, useRef, useState } from 'react'
import { getAllCategories } from '../apiConsumer/categoryConsumer';

function CategorySubcategoryMenu(props){
    const selCatgoriesRef=useRef(props.preset?[props.preset]:[]);
    const selSubcategoriesRef=useRef([]);
    const [categories,setCategories]=useState([]);
    const [selectedCategories,setSelectedCategories]=useState(props.preset?[props.preset]:[]);
    const [selectedSubcategories,setSelectedSubcategories]=useState([]);
    const [expandedCategories,setExpandedCategories]=useState([]);
    
    useEffect(()=>{
        getAllCategories((success,data)=>{
            if(success){
                setCategories(data.slice(1));
            }else{
                console.log("Cannot fetch categories")
            }
        });
    },[]);

    useEffect(()=>{
        if(!props.removeCategory)
            return;
        selCatgoriesRef.current=selCatgoriesRef.current.filter((x)=>x!=props.removeCategory);
        setSelectedCategories(selCatgoriesRef.current);
        const catsWithName=createCategoriesWithName(selCatgoriesRef.current);
        props.setCategories(selCatgoriesRef.current,catsWithName);
    },[props.removeCategory]);

    useEffect(()=>{
        if(!props.removeSubcategory)
            return;
        selSubcategoriesRef.current=selSubcategoriesRef.current.filter((x)=>x!=props.removeSubcategory);
        setSelectedSubcategories(selSubcategoriesRef.current);
        const subsWithName=createSubcategoriesWithName(selSubcategoriesRef.current);
        props.setSubcategories(selSubcategoriesRef.current,subsWithName);
    },[props.removeSubcategory]);

    const selectCategory=(cat)=>{
        if(selCatgoriesRef.current.includes(cat.id)){
            selCatgoriesRef.current=selCatgoriesRef.current.filter((x)=>x!=cat.id);
        }else{
            selCatgoriesRef.current.push(cat.id);
        }
        setSelectedCategories(selCatgoriesRef.current);
        const catsWithName=createCategoriesWithName(selCatgoriesRef.current);
        props.setCategories(selCatgoriesRef.current,catsWithName);
    }

    const createCategoriesWithName=(sCats)=>{
        let catsWithName=[]
        for(const c in sCats){
            const crrCat=categories.filter((x)=>x.id===sCats[c])[0];
            catsWithName.push({id:crrCat.id,name:crrCat.name})
        }
        return catsWithName;
    }

    const selectSubcategory=(sub)=>{
        if(selSubcategoriesRef.current.includes(sub.id)){
            selSubcategoriesRef.current=selSubcategoriesRef.current.filter((x)=>x!=sub.id);
        }else{
            selSubcategoriesRef.current.push(sub.id);
        }
        setSelectedSubcategories(selSubcategoriesRef.current);
        const subsWithName=createSubcategoriesWithName(selSubcategoriesRef.current);
        props.setSubcategories(selSubcategoriesRef.current,subsWithName);
    }

    const createSubcategoriesWithName=(sSubs)=>{
        let subsWithName=[]
        for(const s in sSubs){
            const crrSub=null;
            for(const c in categories){
                if(categories[c].subcategories.filter((x)=>x.id===sSubs[s]).length!=0)
                    crrSub=categories[c].subcategories.filter((x)=>x.id===sSubs[s])[0]
            }
            if(crrSub)
                subsWithName.push({id:crrSub.id,name:crrSub.name,cat:crrSub.category.name})
        }        
        return subsWithName;
    }

    const expandCategory=(cat)=>{
        if(expandedCategories.includes(cat))
            setExpandedCategories(expandedCategories.filter((x)=>x!=cat));
        else
            setExpandedCategories([...expandedCategories,cat]);
    }

    return(
        <div className="categoriesList">
            <div className="categoryButtonTitle">PRODUCT CATEGORIES</div>
            {categories.map(category=>
            <div>
                <div className="categoryButtonContainer">
                    <div className={selectedCategories.includes(category.id)?"categoryButton categoryButtonSelected":"categoryButton"}
                        onClick={()=>selectCategory(category)}>{category.name}</div>
                    <img className="categoriesButtonIcon"
                        onClick={()=>expandCategory(category.id)}
                        src={expandedCategories.includes(category.id)?
                        "/images/category_selected.svg":"/images/category_unselected.svg"}
                    />
                </div>
                    
                {expandedCategories.includes(category.id)&&
                    category.subcategories.map(sub=>
                        <div className={selectedSubcategories.includes(sub.id)?"subcategoryButtonSelected":"subcategoryButton"}
                            onClick={()=>selectSubcategory(sub)}>{sub.name}</div>
                    )}
            </div>
            )}
        </div>
    )
}

export default CategorySubcategoryMenu