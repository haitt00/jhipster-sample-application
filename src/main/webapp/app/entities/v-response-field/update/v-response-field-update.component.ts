import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VResponseFieldFormService, VResponseFieldFormGroup } from './v-response-field-form.service';
import { IVResponseField } from '../v-response-field.model';
import { VResponseFieldService } from '../service/v-response-field.service';
import { IVEndpoint } from 'app/entities/v-endpoint/v-endpoint.model';
import { VEndpointService } from 'app/entities/v-endpoint/service/v-endpoint.service';

@Component({
  selector: 'jhi-v-response-field-update',
  templateUrl: './v-response-field-update.component.html',
})
export class VResponseFieldUpdateComponent implements OnInit {
  isSaving = false;
  vResponseField: IVResponseField | null = null;

  vEndpointsSharedCollection: IVEndpoint[] = [];

  editForm: VResponseFieldFormGroup = this.vResponseFieldFormService.createVResponseFieldFormGroup();

  constructor(
    protected vResponseFieldService: VResponseFieldService,
    protected vResponseFieldFormService: VResponseFieldFormService,
    protected vEndpointService: VEndpointService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVEndpoint = (o1: IVEndpoint | null, o2: IVEndpoint | null): boolean => this.vEndpointService.compareVEndpoint(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vResponseField }) => {
      this.vResponseField = vResponseField;
      if (vResponseField) {
        this.updateForm(vResponseField);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vResponseField = this.vResponseFieldFormService.getVResponseField(this.editForm);
    if (vResponseField.id !== null) {
      this.subscribeToSaveResponse(this.vResponseFieldService.update(vResponseField));
    } else {
      this.subscribeToSaveResponse(this.vResponseFieldService.create(vResponseField));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVResponseField>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vResponseField: IVResponseField): void {
    this.vResponseField = vResponseField;
    this.vResponseFieldFormService.resetForm(this.editForm, vResponseField);

    this.vEndpointsSharedCollection = this.vEndpointService.addVEndpointToCollectionIfMissing<IVEndpoint>(
      this.vEndpointsSharedCollection,
      vResponseField.vEndpoint
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vEndpointService
      .query()
      .pipe(map((res: HttpResponse<IVEndpoint[]>) => res.body ?? []))
      .pipe(
        map((vEndpoints: IVEndpoint[]) =>
          this.vEndpointService.addVEndpointToCollectionIfMissing<IVEndpoint>(vEndpoints, this.vResponseField?.vEndpoint)
        )
      )
      .subscribe((vEndpoints: IVEndpoint[]) => (this.vEndpointsSharedCollection = vEndpoints));
  }
}
