import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVResponseField } from '../v-response-field.model';

@Component({
  selector: 'jhi-v-response-field-detail',
  templateUrl: './v-response-field-detail.component.html',
})
export class VResponseFieldDetailComponent implements OnInit {
  vResponseField: IVResponseField | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vResponseField }) => {
      this.vResponseField = vResponseField;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
