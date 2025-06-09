import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class EnvironmentService {
  public readonly API_URL: string;
  public readonly WS_URL: string;

  constructor() {
    const env = (window as any).env || {};
    this.API_URL = env.API_URL || '/api'; // fallback
    this.WS_URL = env.WS_URL || '/ws/access'; // fallback
  }
}
