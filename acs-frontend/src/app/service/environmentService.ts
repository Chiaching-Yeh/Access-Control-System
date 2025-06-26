import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class EnvironmentService {

  get API_URL(): string {
    if (typeof window !== 'undefined') {
      return (window as any).env?.API_URL || '/api';
    }
    return '/api'; 
  }

  get WS_URL(): string {
    if (typeof window !== 'undefined') {
      return (window as any).env?.WS_URL || '/ws/access';
    }
    return '/ws/access';
  }
  
}
