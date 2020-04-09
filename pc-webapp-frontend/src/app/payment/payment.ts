export class Payment {
    id: number;
    callSid: string;
    amount: string;
    method: string;
    creditCardNumber: string;
    owner: string;
    expirationDate: string;
    securityNumber: string;
    success: boolean;
}
