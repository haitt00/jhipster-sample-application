import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVEndpoint } from '../v-endpoint.model';

@Component({
  selector: 'jhi-v-endpoint-detail',
  templateUrl: './v-endpoint-detail.component.html',
})
export class VEndpointDetailComponent implements OnInit {
  vEndpoint: IVEndpoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vEndpoint }) => {
      this.vEndpoint = vEndpoint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
