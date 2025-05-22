import { Directive, ContentChildren, ContentChild } from '@angular/core';
import { ComplexBase, ArrayBase, setValue } from '@syncfusion/ej2-angular-base';
import { CellsDirective } from './cells.directive';
import * as i0 from "@angular/core";
let input = ['cells', 'customHeight', 'format', 'height', 'hidden', 'index', 'isReadOnly'];
let outputs = [];
/**
 * `e-row` directive represent a row of the Angular Spreadsheet.
 * It must be contained in a `e-sheet` directive.
 * ```html
 * <ejs-spreadsheet>
 *   <e-sheets>
 *    <e-sheet>
 *    <e-rows>
 *    <e-row></e-row>
 *    </e-rows>
 *    </e-sheet>
 *   </e-sheets>
 * </ejs-spreadsheet>
 * ```
 */
export class RowDirective extends ComplexBase {
    constructor(viewContainerRef) {
        super();
        this.viewContainerRef = viewContainerRef;
        this.tags = ['cells'];
        setValue('currentInstance', this, this.viewContainerRef);
        this.registerEvents(outputs);
        this.directivePropList = input;
    }
}
RowDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: RowDirective, deps: [{ token: i0.ViewContainerRef }], target: i0.ɵɵFactoryTarget.Directive });
RowDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: RowDirective, selector: "e-rows>e-row", inputs: { cells: "cells", customHeight: "customHeight", format: "format", height: "height", hidden: "hidden", index: "index", isReadOnly: "isReadOnly" }, queries: [{ propertyName: "childCells", first: true, predicate: CellsDirective, descendants: true }], usesInheritance: true, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: RowDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-rows>e-row',
                    inputs: input,
                    outputs: outputs,
                    queries: {
                        childCells: new ContentChild(CellsDirective)
                    }
                }]
        }], ctorParameters: function () { return [{ type: i0.ViewContainerRef }]; } });
/**
 * Row Array Directive
 * @private
 */
export class RowsDirective extends ArrayBase {
    constructor() {
        super('rows');
    }
}
RowsDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: RowsDirective, deps: [], target: i0.ɵɵFactoryTarget.Directive });
RowsDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: RowsDirective, selector: "e-sheet>e-rows", queries: [{ propertyName: "children", predicate: RowDirective }], usesInheritance: true, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: RowsDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-sheet>e-rows',
                    queries: {
                        children: new ContentChildren(RowDirective)
                    },
                }]
        }], ctorParameters: function () { return []; } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicm93cy5kaXJlY3RpdmUuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi9zcmMvc3ByZWFkc2hlZXQvcm93cy5kaXJlY3RpdmUudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUEsT0FBTyxFQUFFLFNBQVMsRUFBb0IsZUFBZSxFQUFFLFlBQVksRUFBRSxNQUFNLGVBQWUsQ0FBQztBQUMzRixPQUFPLEVBQUUsV0FBVyxFQUFFLFNBQVMsRUFBRSxRQUFRLEVBQUUsTUFBTSw4QkFBOEIsQ0FBQztBQUVoRixPQUFPLEVBQUUsY0FBYyxFQUFFLE1BQU0sbUJBQW1CLENBQUM7O0FBRW5ELElBQUksS0FBSyxHQUFhLENBQUMsT0FBTyxFQUFFLGNBQWMsRUFBRSxRQUFRLEVBQUUsUUFBUSxFQUFFLFFBQVEsRUFBRSxPQUFPLEVBQUUsWUFBWSxDQUFDLENBQUM7QUFDckcsSUFBSSxPQUFPLEdBQWEsRUFBRSxDQUFDO0FBQzNCOzs7Ozs7Ozs7Ozs7OztHQWNHO0FBU0gsTUFBTSxPQUFPLFlBQWEsU0FBUSxXQUF5QjtJQTRDdkQsWUFBb0IsZ0JBQWlDO1FBQ2pELEtBQUssRUFBRSxDQUFDO1FBRFEscUJBQWdCLEdBQWhCLGdCQUFnQixDQUFpQjtRQXhDOUMsU0FBSSxHQUFhLENBQUMsT0FBTyxDQUFDLENBQUM7UUEwQzlCLFFBQVEsQ0FBQyxpQkFBaUIsRUFBRSxJQUFJLEVBQUUsSUFBSSxDQUFDLGdCQUFnQixDQUFDLENBQUM7UUFDekQsSUFBSSxDQUFDLGNBQWMsQ0FBQyxPQUFPLENBQUMsQ0FBQztRQUM3QixJQUFJLENBQUMsaUJBQWlCLEdBQUcsS0FBSyxDQUFDO0lBQ25DLENBQUM7O3lHQWpEUSxZQUFZOzZGQUFaLFlBQVksc1BBSFksY0FBYzsyRkFHdEMsWUFBWTtrQkFSeEIsU0FBUzttQkFBQztvQkFDUCxRQUFRLEVBQUUsY0FBYztvQkFDeEIsTUFBTSxFQUFFLEtBQUs7b0JBQ2IsT0FBTyxFQUFFLE9BQU87b0JBQ2hCLE9BQU8sRUFBRTt3QkFDTCxVQUFVLEVBQUUsSUFBSSxZQUFZLENBQUMsY0FBYyxDQUFDO3FCQUMvQztpQkFDSjs7QUFxREQ7OztHQUdHO0FBT0gsTUFBTSxPQUFPLGFBQWMsU0FBUSxTQUF3QjtJQUN2RDtRQUNJLEtBQUssQ0FBQyxNQUFNLENBQUMsQ0FBQztJQUNsQixDQUFDOzswR0FIUSxhQUFhOzhGQUFiLGFBQWEsK0VBSFksWUFBWTsyRkFHckMsYUFBYTtrQkFOekIsU0FBUzttQkFBQztvQkFDUCxRQUFRLEVBQUUsZ0JBQWdCO29CQUMxQixPQUFPLEVBQUU7d0JBQ0wsUUFBUSxFQUFFLElBQUksZUFBZSxDQUFDLFlBQVksQ0FBQztxQkFDOUM7aUJBQ0oiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgeyBEaXJlY3RpdmUsIFZpZXdDb250YWluZXJSZWYsIENvbnRlbnRDaGlsZHJlbiwgQ29udGVudENoaWxkIH0gZnJvbSAnQGFuZ3VsYXIvY29yZSc7XG5pbXBvcnQgeyBDb21wbGV4QmFzZSwgQXJyYXlCYXNlLCBzZXRWYWx1ZSB9IGZyb20gJ0BzeW5jZnVzaW9uL2VqMi1hbmd1bGFyLWJhc2UnO1xuXG5pbXBvcnQgeyBDZWxsc0RpcmVjdGl2ZSB9IGZyb20gJy4vY2VsbHMuZGlyZWN0aXZlJztcblxubGV0IGlucHV0OiBzdHJpbmdbXSA9IFsnY2VsbHMnLCAnY3VzdG9tSGVpZ2h0JywgJ2Zvcm1hdCcsICdoZWlnaHQnLCAnaGlkZGVuJywgJ2luZGV4JywgJ2lzUmVhZE9ubHknXTtcbmxldCBvdXRwdXRzOiBzdHJpbmdbXSA9IFtdO1xuLyoqXG4gKiBgZS1yb3dgIGRpcmVjdGl2ZSByZXByZXNlbnQgYSByb3cgb2YgdGhlIEFuZ3VsYXIgU3ByZWFkc2hlZXQuXG4gKiBJdCBtdXN0IGJlIGNvbnRhaW5lZCBpbiBhIGBlLXNoZWV0YCBkaXJlY3RpdmUuXG4gKiBgYGBodG1sXG4gKiA8ZWpzLXNwcmVhZHNoZWV0PlxuICogICA8ZS1zaGVldHM+XG4gKiAgICA8ZS1zaGVldD5cbiAqICAgIDxlLXJvd3M+XG4gKiAgICA8ZS1yb3c+PC9lLXJvdz5cbiAqICAgIDwvZS1yb3dzPlxuICogICAgPC9lLXNoZWV0PlxuICogICA8L2Utc2hlZXRzPlxuICogPC9lanMtc3ByZWFkc2hlZXQ+XG4gKiBgYGBcbiAqL1xuQERpcmVjdGl2ZSh7XG4gICAgc2VsZWN0b3I6ICdlLXJvd3M+ZS1yb3cnLFxuICAgIGlucHV0czogaW5wdXQsXG4gICAgb3V0cHV0czogb3V0cHV0cywgICAgXG4gICAgcXVlcmllczoge1xuICAgICAgICBjaGlsZENlbGxzOiBuZXcgQ29udGVudENoaWxkKENlbGxzRGlyZWN0aXZlKVxuICAgIH1cbn0pXG5leHBvcnQgY2xhc3MgUm93RGlyZWN0aXZlIGV4dGVuZHMgQ29tcGxleEJhc2U8Um93RGlyZWN0aXZlPiB7XG4gICAgcHVibGljIGRpcmVjdGl2ZVByb3BMaXN0OiBhbnk7XG5cdFxuICAgIHB1YmxpYyBjaGlsZENlbGxzOiBhbnk7XG4gICAgcHVibGljIHRhZ3M6IHN0cmluZ1tdID0gWydjZWxscyddO1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgY2VsbCBhbmQgaXRzIHByb3BlcnRpZXMgZm9yIHRoZSByb3cuXG4gICAgICogQGRlZmF1bHQgW11cbiAgICAgKi9cbiAgICBwdWJsaWMgY2VsbHM6IGFueTtcbiAgICAvKiogXG4gICAgICogc3BlY2lmaWVzIGN1c3RvbSBoZWlnaHQgb2YgdGhlIHJvdy5cbiAgICAgKiBAZGVmYXVsdCBmYWxzZVxuICAgICAqL1xuICAgIHB1YmxpYyBjdXN0b21IZWlnaHQ6IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIGZvcm1hdCBvZiB0aGUgcm93LlxuICAgICAqIEBkZWZhdWx0IHt9XG4gICAgICovXG4gICAgcHVibGljIGZvcm1hdDogYW55O1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgaGVpZ2h0IG9mIHRoZSByb3cuXG4gICAgICogQGRlZmF1bHQgMjBcbiAgICAgKiBAYXNwdHlwZSBkb3VibGVcbiAgICAgKiBAYXNwZGVmYXVsdHZhbHVlIDIwLjBcbiAgICAgKi9cbiAgICBwdWJsaWMgaGVpZ2h0OiBhbnk7XG4gICAgLyoqIFxuICAgICAqIFRvIGhpZGUvc2hvdyB0aGUgcm93IGluIHNwcmVhZHNoZWV0LlxuICAgICAqIEBkZWZhdWx0IGZhbHNlXG4gICAgICovXG4gICAgcHVibGljIGhpZGRlbjogYW55O1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgdGhlIGluZGV4IHRvIHRoZSByb3cuIEJhc2VkIG9uIHRoZSBpbmRleCwgcm93IHByb3BlcnRpZXMgYXJlIGFwcGxpZWQuXG4gICAgICogQGRlZmF1bHQgMFxuICAgICAqIEBhc3B0eXBlIGludFxuICAgICAqL1xuICAgIHB1YmxpYyBpbmRleDogYW55O1xuICAgIC8qKiBcbiAgICAgKiBSZXByZXNlbnRzIHdoZXRoZXIgYSByb3cgaW4gdGhlIHNoZWV0IGlzIHJlYWQtb25seSBvciBub3QuIElmIHNldCB0byB0cnVlLCBpdCBwcmV2ZW50cyBlZGl0aW5nIHRoZSBzcGVjaWZpZWQgY2VsbCBpbiB0aGUgc2hlZXQuXG4gICAgICogQGRlZmF1bHQgZmFsc2VcbiAgICAgKi9cbiAgICBwdWJsaWMgaXNSZWFkT25seTogYW55O1xuXG4gICAgY29uc3RydWN0b3IocHJpdmF0ZSB2aWV3Q29udGFpbmVyUmVmOlZpZXdDb250YWluZXJSZWYpIHtcbiAgICAgICAgc3VwZXIoKTtcbiAgICAgICAgc2V0VmFsdWUoJ2N1cnJlbnRJbnN0YW5jZScsIHRoaXMsIHRoaXMudmlld0NvbnRhaW5lclJlZik7XG4gICAgICAgIHRoaXMucmVnaXN0ZXJFdmVudHMob3V0cHV0cyk7XG4gICAgICAgIHRoaXMuZGlyZWN0aXZlUHJvcExpc3QgPSBpbnB1dDtcbiAgICB9XG59XG5cbi8qKlxuICogUm93IEFycmF5IERpcmVjdGl2ZVxuICogQHByaXZhdGVcbiAqL1xuQERpcmVjdGl2ZSh7XG4gICAgc2VsZWN0b3I6ICdlLXNoZWV0PmUtcm93cycsXG4gICAgcXVlcmllczoge1xuICAgICAgICBjaGlsZHJlbjogbmV3IENvbnRlbnRDaGlsZHJlbihSb3dEaXJlY3RpdmUpXG4gICAgfSxcbn0pXG5leHBvcnQgY2xhc3MgUm93c0RpcmVjdGl2ZSBleHRlbmRzIEFycmF5QmFzZTxSb3dzRGlyZWN0aXZlPiB7XG4gICAgY29uc3RydWN0b3IoKSB7XG4gICAgICAgIHN1cGVyKCdyb3dzJyk7XG4gICAgfVxufSJdfQ==