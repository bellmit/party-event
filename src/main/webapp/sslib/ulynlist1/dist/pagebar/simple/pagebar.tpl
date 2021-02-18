<div class="simple-pagebar">
   <div id="list-page" class="list-mod" data-spm="1000343" data-module="page" data-spm-max-idx="5">
   <div class="m-pagination">
     <div class="inner pagebottonlist" >
      <span class="text">共{{opts.totalPage}}页，{{opts.totalNum}}条</span>
        {{if opts.currentPage<=1}}
                   <span class="btn-disabled" page="{{opts.currentPage-1}}">上一页</span>
                   {{else}}
                   <a class="btn" page="{{opts.currentPage-1}}">上一页</a>
                   {{/if}}

            {{if opts.currentPage<opts.totalPage}}
             <a class="btn" page="{{opts.currentPage-1+2}}">下一页</a>
            {{else}}
             <span class="btn-disabled" page="{{opts.currentPage-1+2}}">下一页</span>
            {{/if}}

       <span class="text">到第</span>
       <input class="num goto" name="page-num" data-total="{{opts.totalPage}}" data-pagesize="{{opts.totalPage}}" value="{{opts.currentPage}}">
       <span class="text">页</span>
         <input type="button" class="go_button submit" value="确定">
     </div>
   </div>

   </div>
</div>
