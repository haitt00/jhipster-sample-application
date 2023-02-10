import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { VEndpointFormService, VEndpointFormGroup } from './v-endpoint-form.service';
import { IVEndpoint } from '../v-endpoint.model';
import { VEndpointService } from '../service/v-endpoint.service';

@Component({
  selector: 'jhi-v-endpoint-update',
  templateUrl: './v-endpoint-update.component.html',
})
export class VEndpointUpdateComponent implements OnInit {
  isSaving = false;
  vEndpoint: IVEndpoint | null = null;

  editForm: VEndpointFormGroup = this.vEndpointFormService.createVEndpointFormGroup();

  constructor(
    protected vEndpointService: VEndpointService,
    protected vEndpointFormService: VEndpointFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vEndpoint }) => {
      this.vEndpoint = vEndpoint;
      if (vEndpoint) {
        this.updateForm(vEndpoint);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vEndpoint = this.vEndpointFormService.getVEndpoint(this.editForm);
    if (vEndpoint.id !== null) {
      this.subscribeToSaveResponse(this.vEndpointService.update(vEndpoint));
    } else {
      this.subscribeToSaveResponse(this.vEndpointService.create(vEndpoint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVEndpoint>>): void {
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

  protected updateForm(vEndpoint: IVEndpoint): void {
    this.vEndpoint = vEndpoint;
    this.vEndpointFormService.resetForm(this.editForm, vEndpoint);
  }
}
