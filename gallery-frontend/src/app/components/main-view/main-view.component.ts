import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MainService } from 'src/app/services/main.service';
import { RestService } from 'src/app/services/rest.service';

@Component({
  selector: 'app-main-view',
  templateUrl: './main-view.component.html',
  styleUrls: ['./main-view.component.css']
})
export class MainViewComponent implements OnInit {

  registrationForm:FormGroup;
  emailValid = false;
  usernameValid = false;
  passwordValid = false;

  constructor(private formBuilder:FormBuilder,
              private restService:RestService,
              private mainService:MainService,
              private router:Router) {

    this.registrationForm = formBuilder.group({
      email: formBuilder.control('', [Validators.required, Validators.email]),
      username: formBuilder.control('', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]), 
      password: formBuilder.control('', [Validators.required, Validators.minLength(4), Validators.maxLength(99)])
    })
  }

  ngOnInit(): void {
  }

  public onEmailInputChange(){
    // Check on the server only if validator is satisfied
    const control = this.registrationForm.get('email')
    if(control?.valid){
      this.restService.checkIfEmailIsTaken(control.value)
                      .subscribe(taken => { this.emailValid = false}, 
                              notTaken => { this.emailValid = true})
    } else this.emailValid = false
  }
  public onUsernameInputChange(){
    // Check on the server only if validator is satisfied
    const control = this.registrationForm.get('username')
    if(control?.valid){
      this.restService.checkIfUserExists(control.value)
                      .subscribe(taken => { this.usernameValid = false}, 
                              notTaken => { this.usernameValid = true})
    } else this.usernameValid = false
  }

  public onRegistrationFormSubmit(){
    this.mainService.displayLoadingScreen()
    this.restService.register(this.registrationForm.get("email")?.value, 
                              this.registrationForm.get("username")?.value, 
                              this.registrationForm.get("password")?.value)
                    .subscribe(ok => {  this.router.navigate(['/registered'])
                                        this.mainService.hideLoadingScreen()},
                               nok => { this.router.navigate(['/error'])
                                        this.mainService.hideLoadingScreen()})
  }

  public getRegistrationFormControl(name:string){
    return this.registrationForm.get(name)
  }


}
