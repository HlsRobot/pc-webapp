import { Component, OnInit } from '@angular/core';
import { SmsRequest } from './sms-request';
import { SmsServiceService } from './sms-service.service';

@Component({
  selector: 'app-sms',
  templateUrl: './sms.component.html',
  styleUrls: ['./sms.component.css']
})
export class SmsComponent implements OnInit {

  public url = "";

  constructor(private smsService: SmsServiceService) { }

  ngOnInit() {}

  sendMessage(phoneNumber: string, message: string) {

    if ( !this.validE164(phoneNumber)) {
      console.log('fail');
      throw new Error('number must be E164 format!');
    }

    const smsRequest = new SmsRequest();
    smsRequest.phoneNumber = phoneNumber;
    smsRequest.message = message;
    this.smsService.sendMessage(smsRequest, this.url).subscribe(result => "ok");
  }

  getBackendUrl() {
    this.smsService.getBackendUrl().subscribe(
      (result: string) => {
        this.url = result;
      }
    )
  }

  validE164(num: string) {
    return /^\+?[1-9]\d{1,14}$/.test(num);
  }
}
