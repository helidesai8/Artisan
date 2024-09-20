import { AgChartsReact } from "ag-charts-react";

function ArtistCategoryInsight(data)
{
    let categoryData = {
        title: {
          text: "Sales by art category($)",
        },
        data: data.data,
        series: [{ type: "bar", xKey: "name", yKey: "amount", xName: "categories", yName: "Total Amount" }],
        background: {
            fill: 'rgb(246, 246, 246)',
        },
        overlays: {
            noData: {
              text: "No Sales recorded Yet",
            },
          },

    }
    return (
        <div >
            
                <AgChartsReact options={categoryData} />
        </div>
    )
}

export default ArtistCategoryInsight;