package se.ifmo.pepe.coursage.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.ifmo.pepe.coursage.model.Trademarks;
import se.ifmo.pepe.coursage.repository.TrademarkRepository;
import se.ifmo.pepe.coursage.service.TrademarkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/trademark")
public class TrademarkWebController {

    private final TrademarkRepository trademarkRepository;
    private final TrademarkService trademarkService;

    @Autowired
    public TrademarkWebController(TrademarkRepository trademarkRepository, TrademarkService trademarkService) {
        this.trademarkRepository = trademarkRepository;
        this.trademarkService = trademarkService;
    }

    @GetMapping
    public String index() {
        return "/trademark/index.html";
    }

    @RequestMapping(value = "/data_for_datatable", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getDataForDatatable(@RequestParam Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        int length = params.containsKey("length") ? Integer.parseInt(params.get("length").toString()) : 30;
        int start = params.containsKey("start") ? Integer.parseInt(params.get("start").toString()) : 30;
        int currentPage = start / length;

        String sortName = "id";
        String dataTableOrderColumnIdx = params.get("order[0][column]").toString();
        String dataTableOrderColumnName = String.format("columns[%s][data]", dataTableOrderColumnIdx);
        if (params.containsKey(dataTableOrderColumnName))
            sortName = params.get(dataTableOrderColumnName).toString();
        String sortDir = params.containsKey("order[0][dir]") ? params.get("order[0][dir]").toString() : "asc";

        Sort.Order sortOrder = new Sort.Order((sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC), sortName);
        Sort sort = Sort.by(sortOrder);

        Pageable pageRequest = PageRequest.of(currentPage, length, sort);

        String queryString = (String) params.get("search[value]");

        Page<Trademarks> trademarks = trademarkService.getTrademarksForDatable(queryString, pageRequest);

        long totalRecords = trademarks.getTotalElements();

        List<Map<String, Object>> cells = new ArrayList<>();
        trademarks.forEach(trademark -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("id", trademark.getId());
            cellData.put("name", trademark.getName());
            cellData.put("doze", trademark.getDoze());
            cellData.put("release_price", trademark.getReleasePrice());
            cellData.put("drug_id", trademark.getDrugId());
            cellData.put("company_id", trademark.getCompanyId());
            cellData.put("patent_id", trademark.getPatentId());
            cells.add(cellData);
        });

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("draw", draw);
        jsonMap.put("recordsTotal", totalRecords);
        jsonMap.put("recordsFiltered", totalRecords);
        jsonMap.put("data", cells);

        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }
        return json;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Trademarks trademarkInstance = trademarkRepository.findById(Long.valueOf(id)).get();
        model.addAttribute("trademarkInstance", trademarkInstance);

        return "/trademark/edit.html";
    }

    @PostMapping("/update")
    public String update(@Validated @ModelAttribute("trademarkInstance") Trademarks trademarkInstance,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes atts) {
        if (bindingResult.hasErrors()) {
            return "/trademark/edit.html";
        } else {
            if (trademarkRepository.save(trademarkInstance) != null)
                atts.addFlashAttribute("message", "Trademark updated successfully");
            else
                atts.addFlashAttribute("message", "Trademark update failed");

            return "redirect:/trademark";
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("trademarkInstance", new Trademarks());
        return "/trademark/create.html";
    }

    @PostMapping("/save")
    public String save(@Validated @ModelAttribute("trademarkInstance") Trademarks trademarkInstance,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes atts) {
        if (bindingResult.hasErrors()) {
            return "/trademark/create.html";
        } else {
            if (trademarkRepository.save(trademarkInstance) != null)
                atts.addFlashAttribute("message", "Trademark created successfully");
            else
                atts.addFlashAttribute("message", "Trademark create failed");

            return "redirect:/trademark";
        }
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes atts) {
        Trademarks trademarkInstance = trademarkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Customer not found:%d", id)));
        trademarkRepository.delete(trademarkInstance);
        atts.addFlashAttribute("message", "Customer deleted");

        return "redirect:/trademark";
    }
}
