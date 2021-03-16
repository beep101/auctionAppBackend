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
            }else{
                msg="Something went wrong";
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
                    msg=error.response.data.message;
                }else{
                    msg="Something went wrong, please try again";
                }
            }else if(error.request){
                msg="Something went wrong, please try again";
            }else{
                msg="Something went wrong";
            }
            handler(false,msg);
        }
    );
}

export function forgotPassword(email,handler){
    post("forgotPassword",{email}).then(
        (response)=>{
            handler(true,"");
        },
        (error)=>{
            handler(false,"");
        }
    );
}

export function newPassword(token,password,handler){
    post("newPassword",{forgotPasswordToken:token,password:password}).then(
        (response)=>{
            handler(true,"");
        },
        (error)=>{
            handler(false,"");
        }
    );

}

