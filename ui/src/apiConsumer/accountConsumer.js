import { post } from './apiConsumer'

export function login(credentials,handler){
    post("login",credentials).then(
        (response)=>{
            handler(true,response.data.jwt);
        },
        (error)=>{
            let msg="";
            if(error.response){
                if(error.response.status<500){
                    msg="Bad username or password";
                }else{
                    msg="Something went wrong, please try again";
                }
                console.log(error.response);
            }else if(error.request){
                msg="Something went wrong, please try again";
                console.log(error.request);
            }else{
                msg="Something went wrong";
                console.log(error.message);
            }
            handler(false,msg);
        }
    )
}

export function signup(userData,handler){
    post("signup",userData).then(
        (response)=>{
            handler(true,response.data.jwt);
        },
        (error)=>{
            let msg="";
            if(error.response){
                if(error.response.status<500){
                    msg="User with same email alredy exists";
                }else{
                    msg="Something went wrong, please try again";
                }
                console.log(error.response);
            }else if(error.request){
                msg="Something went wrong, please try again";
                console.log(error.request);
            }else{
                msg="Something went wrong";
                console.log(error.message);
            }
            handler(false,msg);
        }
    );
}

export function forgotPassword(email,handler){
    post("forgotPassword",{email:email}).then(
        (response)=>{
            handler(true,"");
        },
        (error)=>{
            handler(false,"");
        }
    );
}

export function newPassword(token,password,handler){
    post("newPassword",{fpToken:token,password:password}).then(
        (response)=>{
            handler(true,"");
        },
        (error)=>{
            handler(false,"");
        }
    );

}

