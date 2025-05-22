import { Directive, ContentChildren, ContentChild } from '@angular/core';
import { ComplexBase, ArrayBase, setValue } from '@syncfusion/ej2-angular-base';
import { ImagesDirective } from './image.directive';
import { ChartsDirective } from './chart.directive';
import * as i0 from "@angular/core";
let input = ['chart', 'colSpan', 'format', 'formula', 'hyperlink', 'image', 'index', 'isLocked', 'isReadOnly', 'notes', 'rowSpan', 'style', 'validation', 'value', 'wrap'];
let outputs = [];
/**
 * `e-cell` directive represent a cell of the Angular Spreadsheet.
 * It must be contained in a `e-row` directive.
 * ```html
 * <ejs-spreadsheet>
 *   <e-sheets>
 *    <e-sheet>
 *    <e-rows>
 *    <e-row>
 *    <e-cells>
 *    <e-cell value='A1'></e-cell>
 *    </e-cells>
 *    </e-row>
 *    </e-rows>
 *    </e-sheet>
 *   </e-sheets>
 * </ejs-spreadsheet>
 * ```
 */
export class CellDirective extends ComplexBase {
    constructor(viewContainerRef) {
        super();
        this.viewContainerRef = viewContainerRef;
        this.tags = ['image', 'chart'];
        setValue('currentInstance', this, this.viewContainerRef);
        this.registerEvents(outputs);
        this.directivePropList = input;
    }
}
CellDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: CellDirective, deps: [{ token: i0.ViewContainerRef }], target: i0.ɵɵFactoryTarget.Directive });
CellDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: CellDirective, selector: "e-cells>e-cell", inputs: { chart: "chart", colSpan: "colSpan", format: "format", formula: "formula", hyperlink: "hyperlink", image: "image", index: "index", isLocked: "isLocked", isReadOnly: "isReadOnly", notes: "notes", rowSpan: "rowSpan", style: "style", validation: "validation", value: "value", wrap: "wrap" }, queries: [{ propertyName: "childImage", first: true, predicate: ImagesDirective, descendants: true }, { propertyName: "childChart", first: true, predicate: ChartsDirective, descendants: true }], usesInheritance: true, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: CellDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-cells>e-cell',
                    inputs: input,
                    outputs: outputs,
                    queries: {
                        childImage: new ContentChild(ImagesDirective),
                        childChart: new ContentChild(ChartsDirective)
                    }
                }]
        }], ctorParameters: function () { return [{ type: i0.ViewContainerRef }]; } });
/**
 * Cell Array Directive
 * @private
 */
export class CellsDirective extends ArrayBase {
    constructor() {
        super('cells');
    }
}
CellsDirective.ɵfac = i0.ɵɵngDeclareFactory({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: CellsDirective, deps: [], target: i0.ɵɵFactoryTarget.Directive });
CellsDirective.ɵdir = i0.ɵɵngDeclareDirective({ minVersion: "12.0.0", version: "13.0.3", type: CellsDirective, selector: "e-row>e-cells", queries: [{ propertyName: "children", predicate: CellDirective }], usesInheritance: true, ngImport: i0 });
i0.ɵɵngDeclareClassMetadata({ minVersion: "12.0.0", version: "13.0.3", ngImport: i0, type: CellsDirective, decorators: [{
            type: Directive,
            args: [{
                    selector: 'e-row>e-cells',
                    queries: {
                        children: new ContentChildren(CellDirective)
                    },
                }]
        }], ctorParameters: function () { return []; } });
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY2VsbHMuZGlyZWN0aXZlLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiLi4vLi4vLi4vLi4vc3JjL3NwcmVhZHNoZWV0L2NlbGxzLmRpcmVjdGl2ZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQSxPQUFPLEVBQUUsU0FBUyxFQUFvQixlQUFlLEVBQUUsWUFBWSxFQUFFLE1BQU0sZUFBZSxDQUFDO0FBQzNGLE9BQU8sRUFBRSxXQUFXLEVBQUUsU0FBUyxFQUFFLFFBQVEsRUFBRSxNQUFNLDhCQUE4QixDQUFDO0FBRWhGLE9BQU8sRUFBRSxlQUFlLEVBQUUsTUFBTSxtQkFBbUIsQ0FBQztBQUNwRCxPQUFPLEVBQUUsZUFBZSxFQUFFLE1BQU0sbUJBQW1CLENBQUM7O0FBRXBELElBQUksS0FBSyxHQUFhLENBQUMsT0FBTyxFQUFFLFNBQVMsRUFBRSxRQUFRLEVBQUUsU0FBUyxFQUFFLFdBQVcsRUFBRSxPQUFPLEVBQUUsT0FBTyxFQUFFLFVBQVUsRUFBRSxZQUFZLEVBQUUsT0FBTyxFQUFFLFNBQVMsRUFBRSxPQUFPLEVBQUUsWUFBWSxFQUFFLE9BQU8sRUFBRSxNQUFNLENBQUMsQ0FBQztBQUNyTCxJQUFJLE9BQU8sR0FBYSxFQUFFLENBQUM7QUFDM0I7Ozs7Ozs7Ozs7Ozs7Ozs7OztHQWtCRztBQVVILE1BQU0sT0FBTyxhQUFjLFNBQVEsV0FBMEI7SUFzRnpELFlBQW9CLGdCQUFpQztRQUNqRCxLQUFLLEVBQUUsQ0FBQztRQURRLHFCQUFnQixHQUFoQixnQkFBZ0IsQ0FBaUI7UUFqRjlDLFNBQUksR0FBYSxDQUFDLE9BQU8sRUFBRSxPQUFPLENBQUMsQ0FBQztRQW1GdkMsUUFBUSxDQUFDLGlCQUFpQixFQUFFLElBQUksRUFBRSxJQUFJLENBQUMsZ0JBQWdCLENBQUMsQ0FBQztRQUN6RCxJQUFJLENBQUMsY0FBYyxDQUFDLE9BQU8sQ0FBQyxDQUFDO1FBQzdCLElBQUksQ0FBQyxpQkFBaUIsR0FBRyxLQUFLLENBQUM7SUFDbkMsQ0FBQzs7MEdBM0ZRLGFBQWE7OEZBQWIsYUFBYSx3WUFKVyxlQUFlLDZFQUNmLGVBQWU7MkZBR3ZDLGFBQWE7a0JBVHpCLFNBQVM7bUJBQUM7b0JBQ1AsUUFBUSxFQUFFLGdCQUFnQjtvQkFDMUIsTUFBTSxFQUFFLEtBQUs7b0JBQ2IsT0FBTyxFQUFFLE9BQU87b0JBQ2hCLE9BQU8sRUFBRTt3QkFDTCxVQUFVLEVBQUUsSUFBSSxZQUFZLENBQUMsZUFBZSxDQUFDO3dCQUM3QyxVQUFVLEVBQUUsSUFBSSxZQUFZLENBQUMsZUFBZSxDQUFDO3FCQUNoRDtpQkFDSjs7QUErRkQ7OztHQUdHO0FBT0gsTUFBTSxPQUFPLGNBQWUsU0FBUSxTQUF5QjtJQUN6RDtRQUNJLEtBQUssQ0FBQyxPQUFPLENBQUMsQ0FBQztJQUNuQixDQUFDOzsyR0FIUSxjQUFjOytGQUFkLGNBQWMsOEVBSFcsYUFBYTsyRkFHdEMsY0FBYztrQkFOMUIsU0FBUzttQkFBQztvQkFDUCxRQUFRLEVBQUUsZUFBZTtvQkFDekIsT0FBTyxFQUFFO3dCQUNMLFFBQVEsRUFBRSxJQUFJLGVBQWUsQ0FBQyxhQUFhLENBQUM7cUJBQy9DO2lCQUNKIiwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgRGlyZWN0aXZlLCBWaWV3Q29udGFpbmVyUmVmLCBDb250ZW50Q2hpbGRyZW4sIENvbnRlbnRDaGlsZCB9IGZyb20gJ0Bhbmd1bGFyL2NvcmUnO1xuaW1wb3J0IHsgQ29tcGxleEJhc2UsIEFycmF5QmFzZSwgc2V0VmFsdWUgfSBmcm9tICdAc3luY2Z1c2lvbi9lajItYW5ndWxhci1iYXNlJztcblxuaW1wb3J0IHsgSW1hZ2VzRGlyZWN0aXZlIH0gZnJvbSAnLi9pbWFnZS5kaXJlY3RpdmUnO1xuaW1wb3J0IHsgQ2hhcnRzRGlyZWN0aXZlIH0gZnJvbSAnLi9jaGFydC5kaXJlY3RpdmUnO1xuXG5sZXQgaW5wdXQ6IHN0cmluZ1tdID0gWydjaGFydCcsICdjb2xTcGFuJywgJ2Zvcm1hdCcsICdmb3JtdWxhJywgJ2h5cGVybGluaycsICdpbWFnZScsICdpbmRleCcsICdpc0xvY2tlZCcsICdpc1JlYWRPbmx5JywgJ25vdGVzJywgJ3Jvd1NwYW4nLCAnc3R5bGUnLCAndmFsaWRhdGlvbicsICd2YWx1ZScsICd3cmFwJ107XG5sZXQgb3V0cHV0czogc3RyaW5nW10gPSBbXTtcbi8qKlxuICogYGUtY2VsbGAgZGlyZWN0aXZlIHJlcHJlc2VudCBhIGNlbGwgb2YgdGhlIEFuZ3VsYXIgU3ByZWFkc2hlZXQuXG4gKiBJdCBtdXN0IGJlIGNvbnRhaW5lZCBpbiBhIGBlLXJvd2AgZGlyZWN0aXZlLlxuICogYGBgaHRtbFxuICogPGVqcy1zcHJlYWRzaGVldD5cbiAqICAgPGUtc2hlZXRzPlxuICogICAgPGUtc2hlZXQ+XG4gKiAgICA8ZS1yb3dzPlxuICogICAgPGUtcm93PlxuICogICAgPGUtY2VsbHM+XG4gKiAgICA8ZS1jZWxsIHZhbHVlPSdBMSc+PC9lLWNlbGw+XG4gKiAgICA8L2UtY2VsbHM+XG4gKiAgICA8L2Utcm93PlxuICogICAgPC9lLXJvd3M+XG4gKiAgICA8L2Utc2hlZXQ+XG4gKiAgIDwvZS1zaGVldHM+XG4gKiA8L2Vqcy1zcHJlYWRzaGVldD5cbiAqIGBgYFxuICovXG5ARGlyZWN0aXZlKHtcbiAgICBzZWxlY3RvcjogJ2UtY2VsbHM+ZS1jZWxsJyxcbiAgICBpbnB1dHM6IGlucHV0LFxuICAgIG91dHB1dHM6IG91dHB1dHMsICAgIFxuICAgIHF1ZXJpZXM6IHtcbiAgICAgICAgY2hpbGRJbWFnZTogbmV3IENvbnRlbnRDaGlsZChJbWFnZXNEaXJlY3RpdmUpLCBcbiAgICAgICAgY2hpbGRDaGFydDogbmV3IENvbnRlbnRDaGlsZChDaGFydHNEaXJlY3RpdmUpXG4gICAgfVxufSlcbmV4cG9ydCBjbGFzcyBDZWxsRGlyZWN0aXZlIGV4dGVuZHMgQ29tcGxleEJhc2U8Q2VsbERpcmVjdGl2ZT4ge1xuICAgIHB1YmxpYyBkaXJlY3RpdmVQcm9wTGlzdDogYW55O1xuXHRcbiAgICBwdWJsaWMgY2hpbGRJbWFnZTogYW55O1xuICAgIHB1YmxpYyBjaGlsZENoYXJ0OiBhbnk7XG4gICAgcHVibGljIHRhZ3M6IHN0cmluZ1tdID0gWydpbWFnZScsICdjaGFydCddO1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgdGhlIGNoYXJ0IG9mIHRoZSBjZWxsLlxuICAgICAqIEBkZWZhdWx0IFtdXG4gICAgICovXG4gICAgcHVibGljIGNoYXJ0OiBhbnk7XG4gICAgLyoqIFxuICAgICAqIFNwZWNpZmllcyB0aGUgY29sdW1uLXdpc2UgY2VsbCBtZXJnZSBjb3VudC5cbiAgICAgKiBAZGVmYXVsdCAxXG4gICAgICogQGFzcHR5cGUgaW50XG4gICAgICovXG4gICAgcHVibGljIGNvbFNwYW46IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIHRoZSBudW1iZXIgZm9ybWF0IGNvZGUgdG8gZGlzcGxheSB2YWx1ZSBpbiBzcGVjaWZpZWQgbnVtYmVyIGZvcm1hdC5cbiAgICAgKiBAZGVmYXVsdCAnR2VuZXJhbCdcbiAgICAgKi9cbiAgICBwdWJsaWMgZm9ybWF0OiBhbnk7XG4gICAgLyoqIFxuICAgICAqIERlZmluZXMgdGhlIGZvcm11bGEgb3IgZXhwcmVzc2lvbiBvZiB0aGUgY2VsbC5cbiAgICAgKiBAZGVmYXVsdCAnJ1xuICAgICAqL1xuICAgIHB1YmxpYyBmb3JtdWxhOiBhbnk7XG4gICAgLyoqIFxuICAgICAqIFNwZWNpZmllcyB0aGUgaHlwZXJsaW5rIG9mIHRoZSBjZWxsLlxuICAgICAqIEBkZWZhdWx0ICcnXG4gICAgICovXG4gICAgcHVibGljIGh5cGVybGluazogYW55O1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgdGhlIGltYWdlIG9mIHRoZSBjZWxsLlxuICAgICAqIEBkZWZhdWx0IFtdXG4gICAgICovXG4gICAgcHVibGljIGltYWdlOiBhbnk7XG4gICAgLyoqIFxuICAgICAqIFNwZWNpZmllcyB0aGUgaW5kZXggb2YgdGhlIGNlbGwuXG4gICAgICogQGRlZmF1bHQgMFxuICAgICAqIEBhc3B0eXBlIGludFxuICAgICAqL1xuICAgIHB1YmxpYyBpbmRleDogYW55O1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgdGhlIGNlbGwgaXMgbG9ja2VkIG9yIG5vdCwgZm9yIGFsbG93IGVkaXQgcmFuZ2UgaW4gc3ByZWFkc2hlZXQgcHJvdGVjdCBvcHRpb24uXG4gICAgICogQGRlZmF1bHQgdHJ1ZVxuICAgICAqL1xuICAgIHB1YmxpYyBpc0xvY2tlZDogYW55O1xuICAgIC8qKiBcbiAgICAgKiBSZXByZXNlbnRzIHdoZXRoZXIgYSBjZWxsIGluIHRoZSBzaGVldCBpcyByZWFkLW9ubHkgb3Igbm90LiBJZiBzZXQgdG8gdHJ1ZSwgaXQgcHJldmVudHMgZWRpdGluZyB0aGUgc3BlY2lmaWVkIGNlbGwgaW4gdGhlIHNoZWV0LlxuICAgICAqIEBkZWZhdWx0IGZhbHNlXG4gICAgICovXG4gICAgcHVibGljIGlzUmVhZE9ubHk6IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIHRoZSBub3RlIG9mIHRoZSBjZWxsLlxuICAgICAqIEBkZWZhdWx0ICcnXG4gICAgICovXG4gICAgcHVibGljIG5vdGVzOiBhbnk7XG4gICAgLyoqIFxuICAgICAqIFNwZWNpZmllcyB0aGUgcm93LXdpc2UgY2VsbCBtZXJnZSBjb3VudC5cbiAgICAgKiBAZGVmYXVsdCAxXG4gICAgICogQGFzcHR5cGUgaW50XG4gICAgICovXG4gICAgcHVibGljIHJvd1NwYW46IGFueTtcbiAgICAvKiogXG4gICAgICogU3BlY2lmaWVzIHRoZSBjZWxsIHN0eWxlIG9wdGlvbnMuIFxuICAgICAqICBcbiAgICAgKiBAZGVmYXVsdCB7fVxuICAgICAqL1xuICAgIHB1YmxpYyBzdHlsZTogYW55O1xuICAgIC8qKiBcbiAgICAgKiBTcGVjaWZpZXMgdGhlIHZhbGlkYXRpb24gb2YgdGhlIGNlbGwuXG4gICAgICogQGRlZmF1bHQgJydcbiAgICAgKi9cbiAgICBwdWJsaWMgdmFsaWRhdGlvbjogYW55O1xuICAgIC8qKiBcbiAgICAgKiBEZWZpbmVzIHRoZSB2YWx1ZSBvZiB0aGUgY2VsbCB3aGljaCBjYW4gYmUgdGV4dCBvciBudW1iZXIuXG4gICAgICogQGRlZmF1bHQgJydcbiAgICAgKi9cbiAgICBwdWJsaWMgdmFsdWU6IGFueTtcbiAgICAvKiogXG4gICAgICogV3JhcHMgdGhlIGNlbGwgdGV4dCB0byB0aGUgbmV4dCBsaW5lLCBpZiB0aGUgdGV4dCB3aWR0aCBleGNlZWRzIHRoZSBjb2x1bW4gd2lkdGguXG4gICAgICogQGRlZmF1bHQgZmFsc2VcbiAgICAgKi9cbiAgICBwdWJsaWMgd3JhcDogYW55O1xuXG4gICAgY29uc3RydWN0b3IocHJpdmF0ZSB2aWV3Q29udGFpbmVyUmVmOlZpZXdDb250YWluZXJSZWYpIHtcbiAgICAgICAgc3VwZXIoKTtcbiAgICAgICAgc2V0VmFsdWUoJ2N1cnJlbnRJbnN0YW5jZScsIHRoaXMsIHRoaXMudmlld0NvbnRhaW5lclJlZik7XG4gICAgICAgIHRoaXMucmVnaXN0ZXJFdmVudHMob3V0cHV0cyk7XG4gICAgICAgIHRoaXMuZGlyZWN0aXZlUHJvcExpc3QgPSBpbnB1dDtcbiAgICB9XG59XG5cbi8qKlxuICogQ2VsbCBBcnJheSBEaXJlY3RpdmVcbiAqIEBwcml2YXRlXG4gKi9cbkBEaXJlY3RpdmUoe1xuICAgIHNlbGVjdG9yOiAnZS1yb3c+ZS1jZWxscycsXG4gICAgcXVlcmllczoge1xuICAgICAgICBjaGlsZHJlbjogbmV3IENvbnRlbnRDaGlsZHJlbihDZWxsRGlyZWN0aXZlKVxuICAgIH0sXG59KVxuZXhwb3J0IGNsYXNzIENlbGxzRGlyZWN0aXZlIGV4dGVuZHMgQXJyYXlCYXNlPENlbGxzRGlyZWN0aXZlPiB7XG4gICAgY29uc3RydWN0b3IoKSB7XG4gICAgICAgIHN1cGVyKCdjZWxscycpO1xuICAgIH1cbn0iXX0=