import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VRequestFieldFormService, VRequestFieldFormGroup } from './v-request-field-form.service';
import { IVRequestField } from '../v-request-field.model';
import { VRequestFieldService } from '../service/v-request-field.service';
import { IVEndpoint } from 'app/entities/v-endpoint/v-endpoint.model';
import { VEndpointService } from 'app/entities/v-endpoint/service/v-endpoint.service';

@Component({
  selector: 'jhi-v-request-field-update',
  templateUrl: './v-request-field-update.component.html',
})
export class VRequestFieldUpdateComponent implements OnInit {
  isSaving = false;
  vRequestField: IVRequestField | null = null;

  vEndpointsSharedCollection: IVEndpoint[] = [];

  editForm: VRequestFieldFormGroup = this.vRequestFieldFormService.createVRequestFieldFormGroup();

  constructor(
    protected vRequestFieldService: VRequestFieldService,
    protected vRequestFieldFormService: VRequestFieldFormService,
    protected vEndpointService: VEndpointService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareVEndpoint = (o1: IVEndpoint | null, o2: IVEndpoint | null): boolean => this.vEndpointService.compareVEndpoint(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vRequestField }) => {
      this.vRequestField = vRequestField;
      if (vRequestField) {
        this.updateForm(vRequestField);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vRequestField = this.vRequestFieldFormService.getVRequestField(this.editForm);
    if (vRequestField.id !== null) {
      this.subscribeToSaveResponse(this.vRequestFieldService.update(vRequestField));
    } else {
      this.subscribeToSaveResponse(this.vRequestFieldService.create(vRequestField));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVRequestField>>): void {
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

  protected updateForm(vRequestField: IVRequestField): void {
    this.vRequestField = vRequestField;
    this.vRequestFieldFormService.resetForm(this.editForm, vRequestField);

    this.vEndpointsSharedCollection = this.vEndpointService.addVEndpointToCollectionIfMissing<IVEndpoint>(
      this.vEndpointsSharedCollection,
      vRequestField.vEndpoint
    );
  }

  protected loadRelationshipsOptions(): void {
    this.vEndpointService
      .query()
      .pipe(map((res: HttpResponse<IVEndpoint[]>) => res.body ?? []))
      .pipe(
        map((vEndpoints: IVEndpoint[]) =>
          this.vEndpointService.addVEndpointToCollectionIfMissing<IVEndpoint>(vEndpoints, this.vRequestField?.vEndpoint)
        )
      )
      .subscribe((vEndpoints: IVEndpoint[]) => (this.vEndpointsSharedCollection = vEndpoints));
  }
}
