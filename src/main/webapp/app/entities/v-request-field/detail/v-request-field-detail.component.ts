import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVRequestField } from '../v-request-field.model';

@Component({
  selector: 'jhi-v-request-field-detail',
  templateUrl: './v-request-field-detail.component.html',
})
export class VRequestFieldDetailComponent implements OnInit {
  vRequestField: IVRequestField | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vRequestField }) => {
      this.vRequestField = vRequestField;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
