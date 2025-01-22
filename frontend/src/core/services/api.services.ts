import axios, { Axios, AxiosError, AxiosRequestConfig, AxiosResponse } from "axios";

class apiService {
    protected axios: Axios
    private readonly apiURL = "#"
    
    constructor(url: string) {
          this.axios= axios.create ({
            baseURL: `${this.apiURL}/${url}`
          })
        }
     

      // // Interceptor para añadir el token de autenticación a las solicitudes
      // this.axios.interceptors.request.use(
      //     (config) => {
      //         const token = localStorage.getItem('authToken');
      //         if (token) {
      //             config.headers.Authorization = `Bearer ${token}`;
      //         }
      //         return config;
      //     },
      //     (error) => Promise.reject(error)
      // );
  
onSuccess = <T>(response: AxiosResponse<T>): T => response.data

onError = (error: AxiosError): Promise<never> => {
  return Promise.reject(error)
}

get = <U>(resource: string, config?: AxiosRequestConfig) =>
  this.axios
    ?.get<U>(`${resource}`, config)
    .then(this.onSuccess)
    .catch(this.onError)

post = <U>(resource: string, body: unknown, config?: AxiosRequestConfig) =>
  this.axios
    .post<U>(`${resource}`, body, config)
    .then(this.onSuccess)
    .catch(this.onError)

patch = <U>(resource: string, body: unknown, config?: AxiosRequestConfig) =>
  this.axios
    .patch<U>(`${resource}`, body, config)
    .then(this.onSuccess)
    .catch(this.onError)

put = (resource: string, body: Record<string, unknown>) =>
  this.axios.put(`${resource}`, body).then(this.onSuccess).catch(this.onError)

delete = (resource: string) =>
  this.axios.delete(resource).then(this.onSuccess).catch(this.onError)

    }

export default apiService