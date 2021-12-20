import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MainService } from 'src/app/services/main.service';
import { RestService } from 'src/app/services/rest.service';

@Component({
  selector: 'app-account-confirmation-view',
  templateUrl: './account-confirmation-view.component.html',
  styleUrls: ['./account-confirmation-view.component.css']
})
export class AccountConfirmationViewComponent implements OnInit {

  ok = false
  nok = false

  constructor(private route: ActivatedRoute,
              private restService: RestService,
              private mainService: MainService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const token = params["token"]
      if(!token) this.nok = true
      else this.restService.confirm(token).subscribe(ok => this.ok = true, nok => this.nok = true)
    })
  }

}
