import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'v-endpoint',
        data: { pageTitle: 'jhipsterSampleApplicationApp.vEndpoint.home.title' },
        loadChildren: () => import('./v-endpoint/v-endpoint.module').then(m => m.VEndpointModule),
      },
      {
        path: 'v-request-field',
        data: { pageTitle: 'jhipsterSampleApplicationApp.vRequestField.home.title' },
        loadChildren: () => import('./v-request-field/v-request-field.module').then(m => m.VRequestFieldModule),
      },
      {
        path: 'v-response-field',
        data: { pageTitle: 'jhipsterSampleApplicationApp.vResponseField.home.title' },
        loadChildren: () => import('./v-response-field/v-response-field.module').then(m => m.VResponseFieldModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
