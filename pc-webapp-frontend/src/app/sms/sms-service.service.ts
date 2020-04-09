import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SmsRequest } from './sms-request';
import { Observable } from 'rxjs';
import {catchError, map} from "rxjs/operators";


@Injectable()
export class SmsServiceService {

  constructor(private http: HttpClient) {
  }

  public sendMessage(smsRequest: SmsRequest, url: string): Observable<SmsRequest> {
    return this.http.post<SmsRequest>(url + 'sms', smsRequest);
  }

    public getBackendUrl(): Observable<string> {
    return this.http.get<string>('http://localhost:8080/api/get_url');
  }

}
