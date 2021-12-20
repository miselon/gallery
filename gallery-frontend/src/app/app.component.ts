import { HttpResponse } from '@angular/common/http';
import { Component, Injectable, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, NavigationEnd, NavigationError, NavigationStart, Router } from '@angular/router';
import { environment } from 'src/environments/environment.prod';
import { AuthService } from './services/auth.service';
import { MainService } from './services/main.service';
import { RestService } from './services/rest.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  clientUrl = environment.baseUrl

  loginForm:FormGroup
  private file?:File

  title = "gallery"
  heading = "gallery"
  currentUser = ""
  alertText = ""

  editModeActive = false
  searchBarActive = false
  alertBarActive = false
  loginFormActive = false
  loading = false
  userLoggedIn = false

  constructor(private formBuilder:FormBuilder,
              private restService:RestService,
              private authService:AuthService,
              private mainService:MainService,
              private router:Router) {
    // Login form
    this.loginForm = formBuilder.group({
      username: formBuilder.control('', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]), 
      password: formBuilder.control('', [Validators.required, Validators.minLength(4), Validators.maxLength(99)])
    })
    // Notification subject
    mainService.notification$.subscribe(text => this.showAlert(text))
    // Loading screen subjects
    mainService.displayLoading$.subscribe(() => this.showLoading())
    mainService.hideLoading$.subscribe(() => this.hideLoading())
  }
  
  ngOnInit(): void {
    if(this.authService.isLoggedIn()) {
      this.userLoggedIn = true
      this.currentUser = this.authService.getCurrentUser()
    } 

    // Router events
    this.router.events.subscribe(event => {
      // Route change finished
      if(event instanceof NavigationEnd) {
        this.reloadHeading()
        this.mainService.reloadGallery()
      }
      // Routing error
      else if(event instanceof NavigationError) this.showAlert("Routing error")
    })
  }

  onLoginButtonClick(){
    this.loginFormActive = !this.loginFormActive
  }
  onLoginFormSubmit(){
    const username = this.loginForm.get("username")?.value
    const password = this.loginForm.get("password")?.value
    // Call login API
    this.restService.login(username, password)
      .subscribe( ok => { 
        // Retrieve token from the HTTP header
        const headers = ok.headers.keys()
        const token = ok.headers.get(headers[0])
        if(!token) return
        // Save user data
        this.authService.setSession(username, token)
        this.userLoggedIn = true
        this.currentUser = username
        // Hide login form
        this.loginFormActive = false
        // Show hello prompt
        this.showAlert("welcome " + username)
        // Reload the gallery view to show edit controls
        this.mainService.reloadGallery()
        this.reloadHeading()

      },nok => {this.showAlert("login failed")})
  }

  onLogoutButtonClick(){
    this.authService.terminateSession()
    this.userLoggedIn = false;
    this.showAlert("goodbye " + this.currentUser)
    this.currentUser = ""
    this.mainService.reloadGallery();
    this.reloadHeading()
  }

  onSearchButtonClick(){
    this.searchBarActive = !this.searchBarActive
  }

  onSearchSubmit(value:string){
    this.restService.checkIfUserExists(value)
                    .subscribe( found => {  this.router.navigate(['/'+value]).then(() => {
                                            this.searchBarActive = false
                                            this.mainService.reloadGallery()})},
                                notFound => {this.showAlert("User " + value + " not found")})
  }

  // Slides out an alert with a given text
  showAlert(text:string){
    if(!this.alertBarActive){
      this.alertText = text;
      this.alertBarActive = true;
      setTimeout(() => this.alertBarActive = false, 3000)
    }
  }
  // Updates main heading based on current route
  reloadHeading(){
    const url = this.router.url
    if(url == "/" || url == "/error" || url == "/registered" || url.startsWith("/confirm")) this.heading = "gallery"
    else if(url.slice(1) == this.currentUser) this.heading = "your gallery"
    else this.heading = url.slice(1) + "'s gallery"
  }
  // Loading screen, timeout to protect it from blocking the app
  showLoading(){
    this.loading = true
    setTimeout(() => {
      this.loading = false
      this.showAlert("something went wrong")
    }, 25000)
  }
  hideLoading(){
    this.loading = false
  }

}
