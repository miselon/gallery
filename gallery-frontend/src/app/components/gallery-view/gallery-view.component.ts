import { Component, OnInit } from '@angular/core';
import { RestService } from 'src/app/services/rest.service';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { MainService } from 'src/app/services/main.service';
import { environment } from 'src/environments/environment.prod';

@Component({
  selector: 'app-gallery-view',
  templateUrl: './gallery-view.component.html',
  styleUrls: ['./gallery-view.component.css']
})
export class GalleryViewComponent implements OnInit {

  apiUrl = environment.apiUrl
  username = ""
  ownGallery = false
  galleryEmpty = false
  ids:string[] = []
  selectedId = ''
  editModeActive = false

  constructor(private restService: RestService,
              private authService: AuthService,
              private mainService: MainService,
              private router: Router,
              private route: ActivatedRoute) {

    this.mainService.reloadGallery$.subscribe(() => this.ngOnInit())
  }

  ngOnInit(): void {
    // Get username from the link
    const name = this.route.snapshot.paramMap.get("username")
    if(!name) return this.handleError()
    // Check if this gallery is owned by the logged-in user
    this.ownGallery = name == this.authService.getCurrentUser()
    // Check if user/gallery exists
    this.restService
      .checkIfUserExists(name)
      .subscribe( response => {if(response) this.username = name
                               else this.router.navigate(['/error'])},
                  error => this.handleError())
    // Get list of thumbnail IDs for this gallery 
    this.restService
      .getImageList(name)
      .subscribe( response => { this.ids = response
                                this.galleryEmpty = this.ids==null || this.ids.length < 1},
                  error => this.handleError()) 
  }

  private handleError(){
    this.router.navigate(['/error'])
  }

  onEditButtonClick(){
    this.editModeActive = !this.editModeActive
  }

  onFileSelected(event:any) {
    const file = event.target.files[0];
    if (file) 
			this.restService.uploadImage(file, this.authService.getToken())
			.subscribe(response => location.reload(),
                 error => this.mainService.displayNotification("Failed to upload the file"))
    else this.mainService.displayNotification("Failed to upload the file")
  }

  onDeleteButtonClick(id:any){
    this.restService.deleteImage(id, this.authService.getToken())
      .subscribe(() => {
        this.ids.splice(this.ids.indexOf(id) , 1)
        this.mainService.displayNotification("Photo deleted successfully")
      })
  }

  onImageClick(id:any){
    if(!this.editModeActive) this.selectedId = id
  }

  exitFullSizeImage(){
    this.selectedId = ""
  }

}
