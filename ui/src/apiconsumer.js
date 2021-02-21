import axios from 'axios';

const url="/api/"

export const login=(credentials)=>{
    axios.post(url+"login", credentials)
    .then(function(response){
        if(response.status==200){
            //set jwt
        }else{
            //set error message
        }
    }).then(function(error){
        //set error message
    });
}

export const signup=(userData)=>{
    axios.post(url+"signup",userData)
    .then(function(response){
        if(response.status==200){
            //set success msg
        }else{
            //set error msg
        }
    }).then(function(error){
        //set error msg
    });
}