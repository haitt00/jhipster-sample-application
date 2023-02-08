import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        data: { pageTitle: 'jhipsterSampleApplicationApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'credit-card',
        data: { pageTitle: 'jhipsterSampleApplicationApp.creditCard.home.title' },
        loadChildren: () => import('./credit-card/credit-card.module').then(m => m.CreditCardModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
