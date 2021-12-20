import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MainService {

  // Notifications
  private notificationSource = new Subject<string>();
  notification$ = this.notificationSource.asObservable();
  // Gallery "refresh"
  private reloadGallerySource = new Subject<boolean>();
  reloadGallery$ = this.reloadGallerySource.asObservable();
  // Display loading screen
  private displayLoadingSource = new Subject<boolean>();
  displayLoading$ = this.displayLoadingSource.asObservable();
  // Hide loading screen
  private hideLoadingSource = new Subject<boolean>();
  hideLoading$ = this.hideLoadingSource.asObservable();

  constructor() { }

  public reloadGallery(){
    this.reloadGallerySource.next(true)
  }
  public displayNotification(text:string){
    this.notificationSource.next(text)
  }
  public displayLoadingScreen(){
    this.displayLoadingSource.next()
  }
  public hideLoadingScreen(){
    this.hideLoadingSource.next()
  }

  
}
