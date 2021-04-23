import axios from 'axios';

const apiClient = axios.create({
    baseURL: "/api/"
});

const defaultErrorHandler=(error,handler)=>{
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

const { get, post, put, delete:del } = apiClient;
export { get, post, put, del,defaultErrorHandler };