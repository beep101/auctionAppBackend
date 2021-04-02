import React, { useCallback, useEffect, useRef, useState } from 'react';
import {getAllCategories} from '../apiConsumer/categoryConsumer';
import {SORTING_SELECT_STYLES, SORTING_SELECT_THEME,WYSIWYG_EDITOR_STYLE} from '../utils/constants';
import Select from 'react-select';
import Dropzone from  'react-dropzone';
import { Editor } from 'react-draft-wysiwyg';
import {EditorState} from 'draft-js';
import { convertFromRaw, convertToRaw } from 'draft-js';
import 'react-draft-wysiwyg/dist/react-draft-wysiwyg.css';

function AddItemStep1(props){
    let data=useRef({
        name:props.data.name,
        description:props.data.description,
        subcategory:props.data.subcategory,
        imageFiles:props.data.imageFiles,
        images:props.data.images
    });

    const [categories,setCategories]=useState([]);
    const [subcategories,setSubcateogories]=useState(props.data.subcategory?
        props.data.subcategory.sub.cat.subs.map(element=>
        {return {value:element.id,label:element.name,cat:props.data.subcategory.sub.cat}}
        ):[]);
    const [msg,setMsg]=useState({});
    const [images,setImages]=useState(props.data.images);
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
        if(data.current.subcategory){
            onClear();
            data.current.subcategory=null;
        }
        setSubcateogories(category.subs.map(element=>{return {value:element.id,label:element.name,cat:category}}));
    }

    const selectSubcategory=(subcategory)=>{
        if(subcategory)
            data.current['subcategory']={id:subcategory.value,sub:subcategory};
    }

    const onDrop = useCallback((acceptedFiles) => {
        console.log([...images,...acceptedFiles])
        setImages([...images,...acceptedFiles]);
        data.current.images=[...data.current.images,...acceptedFiles];
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
        setMsg(msg);
        if(valid)
            props.next(data.current);
    }

    const selectInputRef = useRef();

    const onClear = () => {
      selectInputRef.current.select.clearValue();
    };
    
    return(
        <div className="formContainer" >
            <div className="inputFieldContainer">
                <label className="inputLabel">What do you sell?</label><br/>
                <input className="inputFieldWide" id="name" name="name" onChange={onChange} defaultValue={data.current.name}/>
                {msg.name&&<div className="warningMessageInputLabel">{msg.name}</div>}
            </div>
            <div className="inputFieldContainer categorySelectsInline">
                <span className="categorySelectContainer">
                    {data.current.subcategory?
                    <Select options={categories} isSearchable={false} name="categories" onChange={selectCategory} placeholder="Category"
                        styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} defaultValue={data.current.subcategory.sub.cat}/>
                    :<Select options={categories} isSearchable={false} name="categories" onChange={selectCategory} placeholder="Category"
                        styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} />}
                </span>
                <span className="categorySelectContainer">
                    {data.current.subcategory?
                    <Select options={subcategories} isSearchable={false} name="subcategories" onChange={selectSubcategory} ref={selectInputRef}
                        styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME}  defaultValue={data.current.subcategory.sub} placeholder="Subcategory"/>
                    :<Select options={subcategories} isSearchable={false} name="subcategories" onChange={selectSubcategory}
                        styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} ref={selectInputRef} placeholder="Subcategory"/>}
                </span>
                {msg.cats&&<div className="warningMessageInputLabel">{msg.cats}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Description</label><br/>
                <div className="textEditorContainer">
                    <Editor
                        
                        editorState={editorState}
                        onEditorStateChange={editorUpdate}
                        toolbar={WYSIWYG_EDITOR_STYLE}
                    />
                </div>
                {msg.description&&<div className="warningMessageInputLabel">{msg.description}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Images</label><br/>
                <Dropzone onDrop={onDrop} accept="image/jpg,image/jpeg">
                    {({getRootProps, getInputProps}) => (
                        <section>
                        <div {...getRootProps()} className="dropImagesZone">
                            <input {...getInputProps()} />
                            <p className="simpleText paddingSimpleText">Drag and drop files here, or click to select files</p>
                            {images.length > 0 && <ul >
                                {images.map(image => (
                                    <li className="contactList simpleText">
                                        {image.name}
                                    </li>
                                ))}
                            </ul>}
                        </div>
                        </section>
                    )}
                </Dropzone>
                {msg.images&&<div className="warningMessageInputLabel">{msg.images}</div>}
            </div>
            <div className="inputFieldContainer categorySelectsInline flexToRight">
                <span className="categorySelectContainer">
                    <div className="bidButton" onClick={onNext}>Next</div>
                </span>
            </div>
        </div>
    );
}

export default AddItemStep1