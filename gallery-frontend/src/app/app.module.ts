import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TestComponent } from './components/test/test.component';
import { MainViewComponent } from './components/main-view/main-view.component';
import { GalleryViewComponent } from './components/gallery-view/gallery-view.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ErrorViewComponent } from './components/error-view/error-view.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CookieService } from 'ngx-cookie-service';
import { RegistrationCompleteViewComponent } from './components/registration-complete-view/registration-complete-view.component';
import { AccountConfirmationViewComponent } from './components/account-confirmation-view/account-confirmation-view.component';

@NgModule({
  declarations: [
    AppComponent,
    TestComponent,
    MainViewComponent,
    GalleryViewComponent,
    ErrorViewComponent,
    RegistrationCompleteViewComponent,
    AccountConfirmationViewComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ReactiveFormsModule
  ],
  providers: [
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
