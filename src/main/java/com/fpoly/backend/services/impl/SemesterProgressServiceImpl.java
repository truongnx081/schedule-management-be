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
            throw new AppUnCheckedException("Bạn không có quyền cập nhật SP này", HttpStatus.FORBIDDEN);
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
            throw new AppUnCheckedException("Bạn không có quyền xóa SP này", HttpStatus.FORBIDDEN);
        }

        semesterProgressRepository.delete(existingSP);
    }
}
