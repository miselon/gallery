import { HttpErrorResponse } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AppComponent } from '../app.component';
import { RestService } from './rest.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService{

  private loggedIn = false
  private currentUser = ""
  private token = ""

  constructor() {
    const username = localStorage.getItem("username")
    const token = localStorage.getItem("token")
    if(username && token){
      this.currentUser = username
      this.token = token
      this.loggedIn = true
    } else this.terminateSession()
  }

  // Adds current user to local storage
  public setSession(username:string, token:string){
    localStorage.setItem("token", token)
    localStorage.setItem("username", username)
    this.loggedIn = true
    this.currentUser = username
    this.token = token
  }

  // Removes current user from local storage
  public terminateSession(){
    localStorage.removeItem("token")
    localStorage.removeItem("username")
    this.loggedIn = false
    this.currentUser = ""
    this.token = ""
  }

  public isLoggedIn():boolean{
    return this.loggedIn
  }

  public getCurrentUser():string{
    return this.currentUser
  }

  public getToken():string{
    return this.token
  }

}
