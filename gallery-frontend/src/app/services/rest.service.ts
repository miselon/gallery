import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class RestService {

  private apiUrl = environment.apiUrl

  constructor(private httpClient: HttpClient) { }

  //
  // Secured
  // 
  public uploadImage(file:File, token:string):Observable<any>{

    const headers:HttpHeaders = new HttpHeaders()
                                  .append("Authorization", "Bearer " + token)
    
    const formData = new FormData();
    formData.append("file", file);
    return this.httpClient.post(this.apiUrl + "/files/upload", formData, {headers:headers});
  }
  public deleteImage(id:string, token:string):Observable<any>{
    const headers:HttpHeaders = new HttpHeaders()
                                  .append("Authorization", "Bearer " + token)
    
                                  return this.httpClient.delete(this.apiUrl + "/files/delete?id="+id, {headers:headers});
  }

  //
  // Not secured
  // 
  public register(email:string, username:string, password:string):Observable<string>{
    const body = {
      email: email,
      username: username,
      password: password
    }

    return this.httpClient.post<any>(this.apiUrl + "/users/register", body);
  }
  public confirm(token:string):Observable<any>{
    return this.httpClient.post<any>(this.apiUrl + "/users/confirm?token=" + token, "")
  }

  public getImageList(username:string):Observable<string[]>{
    return this.httpClient.get<string[]>(this.apiUrl + "/files/list?location=" + username)
  }

  public checkIfEmailIsTaken(email:string):Observable<Boolean>{
    return this.httpClient.get<Boolean>(this.apiUrl + "/users/emails?email=" + email)
  }

  public checkIfUserExists(username:string):Observable<Boolean>{
    return this.httpClient.get<Boolean>(this.apiUrl + "/users?username=" + username)
  }

  public login(username:string, password:string):Observable<HttpResponse<any>>{
    const httpOptions = {
      observe: 'response' as const
    };  
    const body = new HttpParams()
      .set('username', username)
      .set('password', password);
    return this.httpClient.post<any>(this.apiUrl + "/login", body, httpOptions);
  }
}
