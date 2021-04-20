import React, { useCallback, useEffect, useState } from 'react';
import Dropzone from 'react-dropzone';

function ImageSetter(props){
    const [image,setImage]=useState(props.image)
    
    useEffect(()=>{
        setImage(props.image);
    },[props.image]);

    const addImage=(file)=>{
        const reader = new FileReader()
        reader.onabort = () => console.log('File reading was aborted')
        reader.onerror = () => console.log('File reading has failed')
        reader.onload = (e) => {
            const imgSplit=e.target.result.split(",");
            const imageData=imgSplit[imgSplit.length-1]
            props.changeImage(imageData)
            setImage(e.target.result);
        }
        reader.readAsDataURL(file);
    }

    const onDrop = useCallback((acceptedFiles) => {
        if(acceptedFiles)
            addImage(acceptedFiles[0]);
    });

    return(
        <div className="imageSetterContainer">
            <div className="profileImageLargeContainer">
                <img className="profileImageLarge" src={image}></img>
            </div>
            <Dropzone onDrop={onDrop} accept="image/jpg,image/jpeg" multiple={false}>
                {({getRootProps, getInputProps}) => (
                    <section>
                    <div {...getRootProps()} className="dropProfileImageZone">
                        <input {...getInputProps()} />
                        Change Image
                    </div>
                    </section>
                )}
            </Dropzone>
        </div>
    )
}

export default ImageSetter