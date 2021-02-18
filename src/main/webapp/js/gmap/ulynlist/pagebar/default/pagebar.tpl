<div class="default-pagebar">
<div class="pagelistdiv">
    <div class="pageinfotext">总计： {{opts.totalPage}} 页 {{opts.totalNum}} 条记录&nbsp;&nbsp;&nbsp;&nbsp;每页 {{opts.linesPerPage}} 条记录</div>
	<div class="pagebottonlist">
        <div class="position_page">
           {{if opts.currentPage<=1}}
            <span class="disabled" page="{{opts.currentPage-1}}">上一页</span>
            {{else}}
            <a page="{{opts.currentPage-1}}">上一页</a>
            {{/if}}

            {{$outPageBtn opts}}

            {{if opts.currentPage<opts.totalPage}}
             <a page="{{opts.currentPage-1+2}}">下一页</a>
            {{else}}
             <span class="disabled" page="{{opts.currentPage-1+2}}">下一页</span>
            {{/if}}
        </div>
        <input type="number" class="goto" min="1" max="{{opts.totalPage}}" value="{{opts.currentPage}}" >
        <input type="button" class="go_button" value="转">
	</div>
</div>
</div>