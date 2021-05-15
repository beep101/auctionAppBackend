import { get, defaultErrorHandler } from './apiConsumer'

export function getPayPalOnboardUrl(token,handler){
    get('onboardingUrl',{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}

export function getClientTokens(token,handler){
    get('clientToken',{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}