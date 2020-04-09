import { Component, OnInit, ViewChild } from '@angular/core';
import { PaymentService } from './payment.service';
import { PaymentRequest } from './payment-request';
import { Payment } from './payment';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {

  public callSid = "";
  public payment = new Payment();
  public paymentList = [];
  public url = "";
  displayedColumns: string[] = ['id', 'callSid', 'method', 'amount', 'creditCardNumber', 'owner',
  'expirationDate', 'securityNumber', 'success'];
  table = new MatTableDataSource(this.paymentList);
  @ViewChild(MatSort) sort: MatSort;

  constructor(private paymentService: PaymentService) { }

  ngOnInit() {
    this.getBackendUrl();
    this.payment = { id: 1, callSid: "123", method: "credit card", amount: "99", creditCardNumber: "12345", 
    owner: "George", expirationDate: "0620", securityNumber: "123", success: false}
    this.getAllPayments();
  }

  sendPaymentRequest(phoneNumber: string, amount: string) {

    if ( !this.validE164(phoneNumber)) {
      console.log('fail');
      throw new Error('number must be E164 format!');
    }

    const paymentRequest = new PaymentRequest();

    paymentRequest.phoneNumber = phoneNumber;
    paymentRequest.amount = amount;
    this.paymentService.sendPaymentRequest(paymentRequest, this.url)
      .subscribe(result => {
        console.log(result);
        this.callSid = result;
        this.getAllPayments();
      });
  }

  getAllPayments() {
    setTimeout(() => {
      if (this.url != "") {
        console.log("Refreshing database status...");
        this.paymentService.getAllPayments(this.url).subscribe(
          (result: Array<Payment>) => {
            this.paymentList = result;
            if (this.callSid != "") {
              let activePayment = this.paymentList.find(i => i.callSid === this.callSid);
              if (!activePayment.success) {
                this.getAllPayments();
              } else {
                console.log("Payment was successfull. Returning to idle mode...");
              }
            }

          });
      } else {
        console.log("Fetching active url ...");
        this.getAllPayments();
      }
    }, 2000);
  }

  getBackendUrl() {
    this.paymentService.getBackendUrl().subscribe(
      (result: string) => {
        console.log(result)
        this.url = result;
      }
    )
  }

  validE164(num: string) {
    return /^\+?[1-9]\d{1,14}$/.test(num);
  }
}
