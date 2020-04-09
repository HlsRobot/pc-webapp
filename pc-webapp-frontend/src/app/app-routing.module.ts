import { NgModule } from '@angular/core';
import { Routes, RouterModule, Router } from '@angular/router';
import { AppComponent } from './app.component';
import { SmsComponent } from './sms/sms.component';
import { PaymentComponent } from './payment/payment.component';

const routes: Routes = [
  { path: '', children: [
    {path: '', component: PaymentComponent},
  ]},
  { path: 'sms', component: SmsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { 
}