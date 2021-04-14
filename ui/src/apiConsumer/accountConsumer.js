import { post } from './apiConsumer'

export function login(credentials,handler){
    post("login",credentials).then(
        (response)=>{
            handler(true,response.data.jwt);
        },
        (error)=>{
            let msg={};
            if(error.response){
                if(error.response.status<500){
                    if(error.response.data.errors){
                        handler(false,error.response.data.errors);
                        return;
                    }else{
                        msg={response:error.response.data.message}
                    }
                }else{
                    msg={response:"Please try again"};
                }
            }else if(error.request){
                msg={response:"Please try again"};
            }else{
                msg={response:"Please try again"};
            }
            handler(false,msg);
        }
    )
}

export function refresh(token,handler){
    post("refresh",{},{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            if(error.response){
                if(error.response.status<500){
                    handler(false,error.response.data);
                }else{
                    handler(false,"Server error");
                }
            }else{
                handler(false,"Something went wrong");
            }
        }
    );
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
                    if(error.response.data.errors){
                        handler(false,error.response.data.errors);
                        return;
                    }else{
                        msg={msg:error.response.data.message}
                    }
                }else{
                    msg={msg:"Something went wrong, please try again"};
                }
            }else if(error.request){
                msg={msg:"Something went wrong, please try again"};
            }else{
                msg={msg:"Something went wrong"};
            }
            handler(false,msg);
        }
    );
}

export function forgotPassword(email,handler){
    post("forgotPassword",{email}).then(
        (response)=>{
            handler(true,{response:'Request successeful, please check your email'});
        },
        (error)=>{
            let msg="";
            if(error.response){
                if(error.response.status<500){
                    if(error.response.data.errors){
                        handler(false,error.response.data.errors);
                        return;
                    }else{
                        msg={response:error.response.data.message}
                    }
                }else{
                    msg={response:"Something went wrong, please try again"};
                }
            }else if(error.request){
                msg={response:"Something went wrong, please try again"};
            }else{
                msg={response:"Something went wrong"};
            }
            handler(false,msg);
        }
    );
}

export function newPassword(token,password,handler){
    post("newPassword",{forgotPasswordToken:token,password:password}).then(
        (response)=>{
            handler(true,"");
        },
        (error)=>{
            let msg="";
            if(error.response){
                if(error.response.status<500){
                    if(error.response.data.errors){
                        handler(false,error.response.data.errors);
                        return;
                    }else{
                        msg={response:error.response.data.message}
                    }
                }else{
                    msg={response:"Something went wrong, please try again"};
                }
            }else if(error.request){
                msg={response:"Something went wrong, please try again"};
            }else{
                msg={response:"Something went wrong"};
            }
            handler(false,msg);
        }
    );

}

