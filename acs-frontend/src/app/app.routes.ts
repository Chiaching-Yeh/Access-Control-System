import { Routes } from '@angular/router';
import { QrPageComponent } from './component/QrPage/qrPage.component';
import { QrSuccessComponent } from './component/QrSuccess/qrSuccess.component';
import { QrFailureComponent } from './component/QrFailure/qrFailure.component';

export const routes: Routes = [
  { path: '', redirectTo: '/qr', pathMatch: 'full' },
  { path: 'qr', component: QrPageComponent },
  { path: 'qr/success', component: QrSuccessComponent },
  { path: 'qr/failure', component: QrFailureComponent },
];
