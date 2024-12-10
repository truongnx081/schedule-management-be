package com.fpoly.backend.services.impl;

import com.fpoly.backend.dto.SemesterProgressDTO;
import com.fpoly.backend.entities.Block;
import com.fpoly.backend.entities.Semester;
import com.fpoly.backend.entities.SemesterProgress;
import com.fpoly.backend.entities.Year;
import com.fpoly.backend.exception.AppUnCheckedException;
import com.fpoly.backend.mapper.SemesterProgressMapper;
import com.fpoly.backend.repository.BlockRepository;
import com.fpoly.backend.repository.SemesterProgressRepository;
import com.fpoly.backend.repository.SemesterRepository;
import com.fpoly.backend.repository.YearRepository;
import com.fpoly.backend.services.IdentifyUserAccessService;
import com.fpoly.backend.services.SemesterProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SemesterProgressServiceImpl implements SemesterProgressService {

    @Autowired
    IdentifyUserAccessService identifyUserAccessService;

    @Autowired
    SemesterProgressMapper semesterProgressMapper;

    @Autowired
    SemesterProgressRepository semesterProgressRepository;

    @Autowired
    BlockRepository blockRepository;

    @Autowired
    SemesterRepository semesterRepository;

    @Autowired
    YearRepository yearRepository;

    @Override
    public SemesterProgressDTO createSemesterProgress(SemesterProgressDTO semesterProgressDTO) {
        String adminCode = identifyUserAccessService.getAdmin().getCode();
        SemesterProgress semesterProgress = semesterProgressMapper.toEntity(semesterProgressDTO);
        semesterProgress.setCreatedBy(adminCode);
        semesterProgress.setBlock(blockRepository.findById(semesterProgressDTO.getBlock()).orElseThrow(() ->
                new AppUnCheckedException("Block not found", HttpStatus.NOT_FOUND)
        ));
        semesterProgress.setSemester(semesterRepository.findById(semesterProgressDTO.getSemester()).orElseThrow(() ->
                new AppUnCheckedException("Semester not found", HttpStatus.NOT_FOUND)
        ));
        semesterProgress.setYear(yearRepository.findById(semesterProgressDTO.getYear()).orElseThrow(() ->
                new AppUnCheckedException("Year not found", HttpStatus.NOT_FOUND)
        ));
        return semesterProgressMapper.toDTO(semesterProgressRepository.save(semesterProgress));
    }


    @Override
    public SemesterProgressDTO updateSemesterProgress(Integer semesterProgressId, SemesterProgressDTO semesterProgressDTO) {
        // Kiểm tra xem ghi chú có tồn tại hay không
        SemesterProgress existingSP = semesterProgressRepository.findById(semesterProgressId)
                .orElseThrow(() -> new AppUnCheckedException("SP không tồn tại", HttpStatus.NOT_FOUND));

        String currentAdminCode = identifyUserAccessService.getAdmin().getCode();

        if (!existingSP.getCreatedBy().equals(currentAdminCode)) {
            throw new AppUnCheckedException("Bạn không có quyền cập nhật tiến trình học kỳ này", HttpStatus.FORBIDDEN);
        }

        existingSP.setCreateDateStart(semesterProgressDTO.getCreateDateStart());
        existingSP.setCreateDateEnd(semesterProgressDTO.getCreateDateEnd());
        existingSP.setRepaireDateStart(semesterProgressDTO.getRepaireDateStart());
        existingSP.setRepaireDateEnd(semesterProgressDTO.getRepaireDateEnd());
        existingSP.setFirstPartStart(semesterProgressDTO.getFirstPartStart());
        existingSP.setFirstPartEnd(semesterProgressDTO.getFirstPartEnd());
        existingSP.setSecondPartStart(semesterProgressDTO.getSecondPartStart());
        existingSP.setSecondPartEnd(semesterProgressDTO.getSecondPartEnd());
        existingSP.setUpdatedBy(currentAdminCode);
        if (semesterProgressDTO.getBlock() != null) {
            Block block = blockRepository.findById(semesterProgressDTO.getBlock())
                    .orElseThrow(() -> new AppUnCheckedException("Loại block không tồn tại", HttpStatus.NOT_FOUND));
            existingSP.setBlock(block);
        }
        if (semesterProgressDTO.getSemester() != null) {
            Semester semester = semesterRepository.findById(semesterProgressDTO.getSemester())
                    .orElseThrow(() -> new AppUnCheckedException("Loại semester không tồn tại", HttpStatus.NOT_FOUND));
            existingSP.setSemester(semester);
        }
        if (semesterProgressDTO.getYear() != null) {
            Year year = yearRepository.findById(semesterProgressDTO.getYear())
                    .orElseThrow(() -> new AppUnCheckedException("Loại năm không tồn tại", HttpStatus.NOT_FOUND));
            existingSP.setYear(year);
        }
        return semesterProgressMapper.toDTO(semesterProgressRepository.save(existingSP));
    }

    @Override
    public void deleteSP(Integer semesterProgressId) {
        SemesterProgress existingSP = semesterProgressRepository.findById(semesterProgressId)
                .orElseThrow(() -> new AppUnCheckedException("Semester Progress không tồn tại", HttpStatus.NOT_FOUND));

        String currentAdminCode = identifyUserAccessService.getAdmin().getCode();

        if (!existingSP.getCreatedBy().equals(currentAdminCode)) {
            throw new AppUnCheckedException("Bạn không có quyền xóa tiến trình học kỳ này", HttpStatus.FORBIDDEN);
        }

        semesterProgressRepository.delete(existingSP);
    }

    @Override
    public SemesterProgress findActivedProgressTrue() {
        return semesterProgressRepository.findByIsActive(true).orElseThrow(()->
                new RuntimeException("SemesterProgress is true not found"));
    }

    @Override
    public List<Map<String, Object>> getAllSemesterProgress() {
        return semesterProgressRepository.getAllSemesterProgress();
    }

    @Override
    public Map<String,String> findCurrentProgress() {
        Date currentDate = new Date();
        SemesterProgress activedSemesterProgress = semesterProgressRepository.findActivedProgress();
        Date createDateStart = activedSemesterProgress.getCreateDateStart();
        Date createDateEnd = activedSemesterProgress.getCreateDateEnd();
        Date prepaireDateStart = activedSemesterProgress.getRepaireDateStart();
        Date prepaireDateEnd = activedSemesterProgress.getRepaireDateEnd();
        Date firstPartDateStart = activedSemesterProgress.getFirstPartStart();
        Date firstPartDateEnd = activedSemesterProgress.getFirstPartEnd();
        Date secondPartDateStart = activedSemesterProgress.getSecondPartStart();
        Date secondPartDateEnd = activedSemesterProgress.getSecondPartEnd();
        HashMap<String,String> result = new HashMap<>();

        if (currentDate.compareTo(createDateStart) < 0){
            result.put("currentProgress", "block-not-begin");
            return result;
        }

        if(currentDate.compareTo(createDateStart) >= 0 && currentDate.compareTo(createDateEnd) <= 0){
            result.put("currentProgress", "create");
            return result;
        }

        if (currentDate.compareTo(prepaireDateStart) >= 0 && currentDate.compareTo(prepaireDateEnd) <= 0){
            result.put("currentProgress", "prepaire");
            return result;
        }

        if (currentDate.compareTo(firstPartDateStart) >= 0 && currentDate.compareTo(firstPartDateEnd) <= 0){
            result.put("currentProgress", "first-part");
            return result;
        }

        if (currentDate.compareTo(secondPartDateStart) >= 0 && currentDate.compareTo(secondPartDateEnd) <= 0){
            result.put("currentProgress", "second-part");
            return result;
        }

        result.put("currentProgress", "block-ended");
        return result;
    }
}
