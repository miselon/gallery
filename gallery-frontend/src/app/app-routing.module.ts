import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountConfirmationViewComponent } from './components/account-confirmation-view/account-confirmation-view.component';
import { ErrorViewComponent } from './components/error-view/error-view.component';
import { GalleryViewComponent } from './components/gallery-view/gallery-view.component';
import { MainViewComponent } from './components/main-view/main-view.component';
import { RegistrationCompleteViewComponent } from './components/registration-complete-view/registration-complete-view.component';

const routes: Routes = [
  // Error view
  {path: 'error', component: ErrorViewComponent},
  // Registration done view
  {path: 'registered', component: RegistrationCompleteViewComponent},
  // Account confirmation
  {path: 'confirm', component: AccountConfirmationViewComponent},
  // Gallery view
  {path: ':username', component: GalleryViewComponent},
  // Home/registration view
  {path: '', component: MainViewComponent},
  // Anything else - redirect to home
  {path: '**', redirectTo: ''}
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
