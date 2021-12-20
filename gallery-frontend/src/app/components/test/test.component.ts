import { Component, OnInit} from '@angular/core';
import { RestService } from 'src/app/services/rest.service';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit{
  
    private file?:File
	public selectedFileName? = "File not selected"

	public ids:string[] = []

    constructor(private restService: RestService) {}
	
	ngOnInit(){
		this.getImageList()
	}

    onFileSelected(event:any) {

        this.file = event.target.files[0];
		this.selectedFileName = this.file?.name
    }

	getImageList(){
		this.restService.getImageList("test")
		.subscribe(response => this.ids = response) 
	}

	uploadSelectedFile(){
		if (this.file) 
			this.restService.uploadImage(this.file, "")
			.subscribe(response => {
				this.file = undefined
				this.selectedFileName = "File not selected"
				this.getImageList()
			})
        else console.log("chuj w dupe")
	}

}
