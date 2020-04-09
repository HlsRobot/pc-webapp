import { Injectable } from '@angular/core';
import { Payment } from './payment';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PaymentRequest } from './payment-request';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http: HttpClient) {
  }

  public sendPaymentRequest(paymentRequest: PaymentRequest, url: string): Observable<string> {
    const headers = new HttpHeaders().set('Content-Type', 'text/plain; charset=utf-8');

    return this.http.post<string>(url + 'call', paymentRequest,
    {responseType:  'text' as 'json'})
    .pipe(
      map((response: string) => response)
    )
  }

  public getPayment(callSid: string, url: string): Observable<Payment> {
    return this.http.get<Payment>(url + 'update?callSid=' + callSid);
  }

  public getAllPayments(url: string): Observable<Array<Payment>> {
    return this.http.get<Array<Payment>>(url + 'get_payments');
  }

  public getBackendUrl(): Observable<string> {
    return this.http.get<string>('https://15da1a0b.ngrok.io/api/get_url', {responseType: 'text' as 'json'});
  }
}
