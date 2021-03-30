import React, { useCallback, useEffect, useRef, useState } from 'react';
import {getAllCategories} from '../apiConsumer/categoryConsumer';
import {SORTING_SELECT_STYLES, SORTING_SELECT_THEME} from '../utils/constants';
import Select from 'react-select';
import Dropzone from  'react-dropzone';
import {Editor, EditorState} from 'draft-js';
import { convertFromRaw, convertToRaw } from 'draft-js';
import 'draft-js/dist/Draft.css';

function AddItemStep1(props){
    let data=useRef({
        name:props.data.name,
        description:props.data.description,
        subcategory:props.data.subcategory,
        imageFiles:props.data.imageFiles,
        images:props.data.images
    });

    const [categories,setCategories]=useState([]);
    const [subcategories,setSubcateogories]=useState([]);
    const [msg,setMsg]=useState({});
    const [images,setImages]=useState(data.current.images);
    const [editorState,setEditorState]=useState(()=>data.current.description?
    EditorState.createWithContent(convertFromRaw(data.current.description)):EditorState.createEmpty());

    const editorUpdate=(state)=>{
        data.current.description=convertToRaw(state.getCurrentContent());
        setEditorState(state);
    }

    useEffect(
        ()=>{getAllCategories((success,data)=>{
            if(success){
                let adaptedCategories=data.slice(1).map(element=>{return {value:element.id,label:element.name,subs:element.subcategories}});
                setCategories(adaptedCategories);
            }else{
                console.log("Cannot fetch categories")
            }
        });
    },[]);

    const onChange=(e)=>{
        data.current[e.target.name]=e.target.value;
    }

    const selectCategory=(category)=>{
        setSubcateogories(category.subs.map(element=>{return {value:element.id,label:element.name,cat:category}}));
    }

    const selectSubcategory=(subcategory)=>{
        data.current['subcategory']={id:subcategory.value,sub:subcategory};
    }

    const onDrop = useCallback((acceptedFiles) => {
        setImages([...images,...acceptedFiles]);
        data.current.images=[...acceptedFiles,...data.current.images];
        acceptedFiles.forEach((file) => {
          const reader = new FileReader()
          reader.onabort = () => console.log('File reading was aborted')
          reader.onerror = () => console.log('File reading has failed')
          reader.onload = () => {
            data.current.imageFiles.push(reader.result);
          }
          reader.readAsDataURL(file)
        })
        
      },
    [])

    const onNext=()=>{
        let valid=true;
        let msg={}
        /*
        if(!data.current.name||!data.current.name.length>0){
            valid=false;
            msg.name="Name can't be empty";
        }
        if(!data.current.description){
            valid=false;
            msg.description="Description can't be empty";
        }
        if(!data.current.subcategory){
            valid=false;
            msg.cats="Category and subcategory must be selected";
        }
        if(!data.current.imageFiles||data.current.imageFiles.length<3){
            valid=false;
            msg.images="At least 3 images required";
        }
        setMsg(msg);*/
        if(valid)
            props.next(data.current);
    }
    
    return(
        <div className="formContainer" >
            <div className="inputFieldContainer">
                <label className="inputLabel">What do you sell?</label><br/>
                <input className="inputFieldWide" id="name" name="name" onChange={onChange} defaultValue={data.current.name}/>
                {msg.name&&<div className="warningMessageInputLabel">{msg.name}</div>}
            </div>
            <div className="inputFieldContainer">
                {data.current.subcategory?
                <Select options={categories} isSearchable={false} name="categories" onChange={selectCategory}
                    styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} defaultValue={data.current.subcategory.sub.cat}/>
                :<Select options={categories} isSearchable={false} name="categories" onChange={selectCategory}
                styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME}/>}
                {data.current.subcategory?
                <Select options={subcategories} isSearchable={false} name="subcategories" onChange={selectSubcategory}
                    styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME}  defaultValue={data.current.subcategory.sub}/>
                :<Select options={subcategories} isSearchable={false} name="subcategories" onChange={selectSubcategory}
                    styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME}/>
                }
                {msg.cats&&<div className="warningMessageInputLabel">{msg.cats}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Description</label><br/>
                <Editor id="description" name="description" editorState={editorState} onChange={editorUpdate} />
                {msg.description&&<div className="warningMessageInputLabel">{msg.description}</div>}
            </div>
            <div className="inputFieldContainer">
                <Dropzone onDrop={onDrop} accept="image/jpg,image/jpeg">
                    {({getRootProps, getInputProps}) => (
                        <section>
                        <div {...getRootProps()}>
                            <input {...getInputProps()} />
                            <p>Drag 'n' drop some files here, or click to select files</p>
                        </div>
                        <ul className="list-group mt-2">
                            {images.length > 0 && images.map(image => (
                                <li className="list-group-item list-group-item-success">
                                {image.name}
                                </li>
                            ))}
                        </ul>
                        </section>
                    )}
                </Dropzone>
                {msg.images&&<div className="warningMessageInputLabel">{msg.images}</div>}
            </div>
            <div className="inputFieldContainer">
                <div className="bidButton" onClick={onNext}>Next</div>
            </div>
        </div>
    );
}

export default AddItemStep1